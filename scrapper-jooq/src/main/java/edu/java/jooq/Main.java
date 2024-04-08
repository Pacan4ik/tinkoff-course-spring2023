package edu.java.jooq;

import edu.java.jooq.codegen.JooqGenerator;

public class Main {
    private Main() {
    }

    public static void main(String[] args) throws Exception {
        JooqGenerator jooqGenerator = new JooqGenerator();
        jooqGenerator.generate();
    }
}
