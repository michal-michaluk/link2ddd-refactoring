package architecture.verify;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ShortagesDependenciesTest {

    @Test
    public void checkOutgoingDependencies() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages(
                        "services", "tools",
                        "dao", "entities", "enums",
                        "api", "external",
                        "shortages", "infrastructure");

        ArchRule rule = classes().that()
                .resideInAnyPackage("shortages..")
                .should().onlyBeAccessed()
                .byClassesThat().resideInAnyPackage("shortages..", "infrastructure");

        rule.check(importedClasses);
    }
}
