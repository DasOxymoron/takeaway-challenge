package de.uko.takeaway;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArchTest {

    private JavaClasses importedClasses;

    @BeforeEach
    public void setup() {
        importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("de.uko.takeaway");
    }

    @Test
    void layers() {
        layeredArchitecture().consideringAllDependencies()
            .layer("rest-api").definedBy("de.uko.takeaway.api.rest..")
            .layer("async-api").definedBy("de.uko.takeaway.api.rest..")
            .layer("domain").definedBy("de.uko.takeaway.domain..")
            .layer("persistence").definedBy("de.uko.takeaway.persistence..")
            .whereLayer("domain").mayOnlyBeAccessedByLayers("rest-api")
            .whereLayer("persistence").mayOnlyBeAccessedByLayers("async-api") //shortcut
        ;
    }



    @Test
    void properNaming() {
        classes().that()
            .resideInAPackage("..exception..")
            .should().haveSimpleNameEndingWith("Exception")
            .check(importedClasses);

        classes().that()
            .resideInAPackage("..controller..")
            .should().haveSimpleNameEndingWith("Controller")
            .orShould().haveSimpleName("DefaultControllerAdvice")
            .check(importedClasses);

        classes().that()
            .resideInAPackage("..dto..")
            .should().haveSimpleNameEndingWith("Dto")
            .check(importedClasses);

        classes().that()
            .resideInAPackage("..mappers..")
            .should().haveSimpleNameEndingWith("Mapper")
            .check(importedClasses);

        classes().that()
            .resideInAPackage("..config..")
            .or()
            .resideInAPackage("..configuration..")
            .should()
            .haveSimpleNameEndingWith("Config")
            .orShould()
            .haveSimpleNameEndingWith("Configuration")
            .check(importedClasses);

        classes().that()
            .resideInAPackage("..port..")
            .should()
            .haveSimpleNameEndingWith("Port")
            .check(importedClasses);

        classes().that()
            .resideInAPackage("..entity..")
            .should().haveSimpleNameEndingWith("Entity")
            .orShould().haveSimpleNameEndingWith("EntityBuilder")
            .check(importedClasses);

        classes().that()
            .resideInAPackage("..repository..")
            .should().haveSimpleNameEndingWith("Repository")
            .check(importedClasses);
    }
}
