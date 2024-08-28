package com.axiom.patienttracker.repositories

import io.getquill.* 
import io.getquill.jdbczio.Quill

object Repository:
    def quillLayer =
        Quill.Postgres.fromNamingStrategy(SnakeCase) //quill instance

    def dataSourceLayer = 
        Quill.DataSource.fromPrefix("patienttracker.db")

    val dataLayer = 
        dataSourceLayer >>> quillLayer
        //data source layer first
        // data source feeds into the quill layer
        // as quill layer depends upon data source layer