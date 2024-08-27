package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.User
import com.axiom.patienttracker.repositories.UserRepository
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

trait UserService:
    def registerUser(email: String, password: String): Task[User]
    def verifyPassword(email: String, password: String): Task[Boolean]

class UserServiceLive private (userRepo: UserRepository) extends UserService:
    override def registerUser(email: String, password: String): Task[User] = 
        userRepo.create(
            User(
                id = -1L,
                email = email,
                hashedPassword = UserServiceLive.Hasher.generateHash(password)
            )
        )
    override def verifyPassword(email: String, password: String): Task[Boolean] = 
        for{
            existingUser <- userRepo.getByEmail(email).someOrFail(new RuntimeException(s"Cannot verify the email: $email"))
            result <- ZIO.attempt(
                UserServiceLive.Hasher.validateHash(password, existingUser.hashedPassword)
            )
        } yield result

object UserServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[UserRepository]
        }yield new UserServiceLive(repo)
    }

    object Hasher:
        // string + salt + nIteration PBKDF2
        private val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA512"
        // PBKDF2
        // Hmac: Hash-based Message Authentication code
        // Crypto Algo: SHA 512: Secure Hash Algorithm 512
        private val PBKDF2_ITERATIONS: Int = 1000
        private val SALT_BYTE_SIZE: Int = 24
        private val HASH_BYTE_SIZE: Int = 24
        private val skf: SecretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM)
        

        private def pbkdf2(message: Array[Char], salt: Array[Byte], iterations: Int, nBytes: Int): Array[Byte] = 
            // Password Based Encryption
            val keySpec: PBEKeySpec = new PBEKeySpec(message, salt, iterations, nBytes * 8) // * 8 because it should be in bits
            skf.generateSecret(keySpec).getEncoded()

        //Array[Byte] there might be some unprintable or inreadable chars to we need to convert them into Hex
        private def toHex(array: Array[Byte]): String =  //heax-encoded bytes
            array.map(byte => "%02X".format(byte)).mkString // each pair of two represent one-single byte

        private def fromHex(string: String): Array[Byte] = 
            string.sliding(2,2).toArray.map{hexValue =>
                Integer.parseInt(hexValue, 16).toByte
            }

        // comparing using XOR as it is in byte
        private def compareBytes(a: Array[Byte], b: Array[Byte]): Boolean = 
            val range = 0 until math.min(a.length, b.length)
            val diff = range.foldLeft(a.length ^ b.length){
                case (acc, i) => acc | (a(i) ^ b(i))
            }
            diff == 0
        
        // salt: random string of fixed size
        // PBKDF2: Password Based Key Derivation Function 2
        def generateHash(password: String): String = 
            val rng: SecureRandom = new SecureRandom()
            val salt: Array[Byte] = Array.ofDim[Byte](SALT_BYTE_SIZE)
            rng.nextBytes(salt) // creates 24 random bytes
            val hasdedBytes = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE)
            s"$PBKDF2_ITERATIONS:${toHex(salt)}:${toHex(hasdedBytes)}"

        def validateHash(password: String, hash: String): Boolean = 
            // parse hash
            val hashSegment = hash.split(":")
            val nIterations = hashSegment(0).toInt
            val salt = fromHex(hashSegment(1))
            val validHash = fromHex(hashSegment(2))
            val testHash = pbkdf2(password.toCharArray(), salt, nIterations, HASH_BYTE_SIZE)
            compareBytes(testHash, validHash)

object UserServiceDemo:
    def main(args: Array[String]) = 
        println(UserServiceLive.Hasher.generateHash("AuroraConstellations"))
        println(UserServiceLive.Hasher.validateHash("AuroraConstellations", "1000:6DEC75EC0B6EB70CA1E1AE2B0456FEC26D207DD928CB179A:F3C4D51B1D332E90B90839C22A0685CC985031742C3AC272"))