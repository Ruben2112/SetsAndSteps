package com.heveamobile.setsandsteps.core.data.config

// The anon key is a publishable, client-embedded credential by design (not a
// secret). Access control is enforced by Postgres RLS policies, not by
// keeping this key hidden.
object SupabaseConfig {
    const val URL = "https://ehadcckmbyzhnzpvvqew.supabase.co"
    const val ANON_KEY =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVoYWRjY2ttYnl6aG56cHZ2cWV3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODM0MjA2NDQsImV4cCI6MjA5ODk5NjY0NH0.SylcSLYFbOOLdaK0aDk4RW8dzlDQky4deNbVuVWWJA4"
}
