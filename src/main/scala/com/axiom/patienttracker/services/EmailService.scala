package com.axiom.patienttracker.services

import zio.*
import java.util.Properties
import jakarta.mail.Session
import jakarta.mail.Authenticator
import jakarta.mail.PasswordAuthentication
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.InternetAddress
import jakarta.mail.Message
import jakarta.mail.Transport
import com.axiom.patienttracker.config.EmailServiceConfig
import com.axiom.patienttracker.config.Configs

trait EmailService:
    def sendEmail(to: String, subject: String, content: String): Task[Unit]
    def sendPasswordRecoveryEmail(to: String, token: String): Task[Unit] = 
        val subject = "Patient Tracker: One Time Password (OTP) for Password Recovery"
        val content = s"""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Password Recovery</title>
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;">
                <div style="max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                    <h2 style="color: #333333;">Patient Tracker Password Recovery</h2>
                    <p style="color: #555555;">Hello,</p>
                    <p style="color: #555555;">You requested to reset your password. Please find your One Time Password below:</p>
                    <p style="text-align: center;">
                        <strong>$token</strong>
                    </p>
                    <p style="color: #555555;">If you did not request this, please ignore this email.</p>
                    <p style="color: #555555;">Thank you,<br>Aurora Constellations</p>
                </div>
            </body>
            </html>
        """
        sendEmail(to, subject, content)

class EmailServiceLive private (emailConfig: EmailServiceConfig) extends EmailService:
    private val host: String = emailConfig.host
    private val port: Int = emailConfig.port
    private val user: String = emailConfig.user
    private val password: String = emailConfig.password

    override def sendEmail(to: String, subject: String, content: String): Task[Unit] = 
        val messageZIO = for {
            properties <- propResource
            session <- createSession(properties)
            message <- createMessage(session)("yamethiy@lakeheadu.ca", to, subject, content)
        } yield (message)
        messageZIO.map(message => Transport.send(message))

    private val propResource: Task[Properties] = {
        val prop = new Properties
        prop.put("mail.smtp.auth", true) // yes we are going to login using email and password
        prop.put("mail.smtp.starttls.enable", "true")
        prop.put("mail.smtp.host", host)
        prop.put("mail.smtp.port", port)
        prop.put("mail.smtp.ssl.trust", host)
        ZIO.succeed(prop)
    }

    private def createSession(prop: Properties): Task[Session] = ZIO.attempt {
        Session.getInstance(prop, new Authenticator {
            override protected def getPasswordAuthentication(): PasswordAuthentication = 
                new PasswordAuthentication(user, password)
        })
    }

    private def createMessage(session: Session)(from: String, to: String, subject: String, content: String): Task[MimeMessage] =
        val message = new MimeMessage(session)
        message.setFrom(from)
        message.setRecipients(
            Message.RecipientType.TO, //We can also use CC or BCC
            to
        )
        message.setSubject(subject)
        message.setContent(content, "text/html; charset=utf-8")
        ZIO.succeed(message)


object EmailServiceLive:
    val layer = ZLayer{
        for{
            emailConfig <- ZIO.service[EmailServiceConfig]
        } yield new EmailServiceLive(emailConfig)
    }
    val configuredLayer = Configs.makeLayer[EmailServiceConfig]("patienttracker.email") >>> layer

object EmailServiceDemo extends ZIOAppDefault:
    val program = for {
        emailService <- ZIO.service[EmailService]
        _ <- emailService.sendPasswordRecoveryEmail("crazycoder@life.com", "ABCD1234")
        _ <- Console.printLine("Email Sent!")
    } yield ()
    override def run: ZIO[Any & (ZIOAppArgs & Scope), Any, Any] = 
        program.provide(EmailServiceLive.configuredLayer)
