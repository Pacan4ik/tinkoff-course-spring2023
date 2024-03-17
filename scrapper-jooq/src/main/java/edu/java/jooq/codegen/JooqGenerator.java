package edu.java.jooq.codegen;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Matchers;
import org.jooq.meta.jaxb.Property;
import org.jooq.meta.jaxb.Strategy;
import org.jooq.meta.jaxb.Target;

public class JooqGenerator {
    private static final Database DATABASE = new Database()
        .withName("org.jooq.meta.extensions.liquibase.LiquibaseDatabase")
        .withProperties(
            new Property().withKey("rootPath").withValue("migrations"),
            new Property().withKey("scripts").withValue("master.xml")
        );

    private static final Generate OPTIONS = new Generate()
        .withGeneratedAnnotation(true)
        .withGeneratedAnnotationDate(false)
        .withNullableAnnotation(true)
        .withNullableAnnotationType("org.jetbrains.annotations.Nullable")
        .withNonnullAnnotation(true)
        .withNonnullAnnotationType("org.jetbrains.annotations.NotNull")
        .withJpaAnnotations(false)
        .withValidationAnnotations(true)
        .withSpringAnnotations(true)
        .withConstructorPropertiesAnnotation(true)
        .withConstructorPropertiesAnnotationOnPojos(true)
        .withConstructorPropertiesAnnotationOnRecords(true)
        .withFluentSetters(false)
        .withDaos(false)
        .withPojos(true);

    private static final Target TARGET = new Target()
        .withPackageName("edu.java.scrapper.domain.jooq")
        .withDirectory("scrapper/src/main/java");

    private static final Configuration CONFIGURATION = new Configuration()
        .withGenerator(
            new Generator()
                .withDatabase(DATABASE)
                .withGenerate(OPTIONS)
                .withTarget(TARGET)
        )
        .withJdbc(new Jdbc()
            .withDriver("org.postgresql.Driver")
        );

    public void generate() throws Exception {
        GenerationTool.generate(CONFIGURATION);
    }
}
