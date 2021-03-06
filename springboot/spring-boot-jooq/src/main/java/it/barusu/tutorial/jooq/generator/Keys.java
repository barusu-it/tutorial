/*
 * This file is generated by jOOQ.
 */
package it.barusu.tutorial.jooq.generator;


import it.barusu.tutorial.jooq.generator.tables.PUser;
import it.barusu.tutorial.jooq.generator.tables.SchemaVersion;
import it.barusu.tutorial.jooq.generator.tables.records.PUserRecord;
import it.barusu.tutorial.jooq.generator.tables.records.SchemaVersionRecord;

import javax.annotation.Generated;

import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>hj_parcel</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<PUserRecord, Long> IDENTITY_P_USER = Identities0.IDENTITY_P_USER;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<PUserRecord> KEY_P_USER_PRIMARY = UniqueKeys0.KEY_P_USER_PRIMARY;
    public static final UniqueKey<SchemaVersionRecord> KEY_SCHEMA_VERSION_PRIMARY = UniqueKeys0.KEY_SCHEMA_VERSION_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<PUserRecord, Long> IDENTITY_P_USER = Internal.createIdentity(PUser.P_USER, PUser.P_USER.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<PUserRecord> KEY_P_USER_PRIMARY = Internal.createUniqueKey(PUser.P_USER, "KEY_p_user_PRIMARY", PUser.P_USER.ID);
        public static final UniqueKey<SchemaVersionRecord> KEY_SCHEMA_VERSION_PRIMARY = Internal.createUniqueKey(SchemaVersion.SCHEMA_VERSION, "KEY_schema_version_PRIMARY", SchemaVersion.SCHEMA_VERSION.VERSION);
    }
}
