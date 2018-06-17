package org.beanfiller.annotation.creator;

import org.beanfiller.annotation.annotations.FunctionDef;
import org.beanfiller.annotation.annotations.Var;
import org.beanfiller.annotation.writer.SystemTestDefWriter;
import org.cornutum.tcases.SystemTestDef;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXParseException;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SystemTestDefCreatorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @FunctionDef
    private static class Sample1 {
        @Var
        private Boolean varDef;
    }

    @Test
    public void create() {
        SystemTestDefCreator fooSystemCreator0 = new SystemTestDefCreator("fooSystem");
        assertThat(fooSystemCreator0).isNotNull();
        SystemTestDef fooSystem0 = fooSystemCreator0.create();
        assertThat(fooSystem0).isNotNull();
        assertThat(fooSystem0.getName()).isEqualTo("fooSystem");
        assertThat(fooSystem0.getFunctionTestDefs()).isEmpty();

        SystemTestDefCreator fooSystemCreator1 = new SystemTestDefCreator("fooSystem", Sample1.class);
        assertThat(fooSystemCreator1).isNotNull();
        SystemTestDef fooSystem1 = fooSystemCreator1.create();
        assertThat(fooSystem1).isNotNull();
        assertThat(fooSystem1.getName()).isEqualTo("fooSystem");
        assertThat(fooSystem1.getFunctionTestDefs()).hasOnlyOneElementSatisfying(f ->
                assertThat(f.getName()).isEqualTo("Sample1"));


        SystemTestDefCreator fooSystemCreator2 = new SystemTestDefCreator(fooSystem1, "fooSystem2", Sample1.class);
        assertThat(fooSystemCreator2).isNotNull();
        SystemTestDef fooSystem2 = fooSystemCreator1.create();
        assertThat(fooSystem2).isNotNull();
        assertThat(fooSystem2.getName()).isEqualTo("fooSystem");
        assertThat(fooSystem2.getFunctionTestDefs()).hasOnlyOneElementSatisfying(f ->
                assertThat(f.getName()).isEqualTo("Sample1"));

        Path outputpath = folder.getRoot().toPath().resolve("foo.xml");
        new SystemTestDefWriter().writeSystemDefToFile(fooSystem1, outputpath);

        SystemTestDefCreator fooSystemCreator3 = new SystemTestDefCreator(outputpath.toString(), "fooSystem", Sample1.class);
        assertThat(fooSystemCreator3).isNotNull();
        SystemTestDef fooSystem3 = fooSystemCreator1.create();
        assertThat(fooSystem3).isNotNull();
        assertThat(fooSystem3.getName()).isEqualTo("fooSystem");
        assertThat(fooSystem3.getFunctionTestDefs()).hasOnlyOneElementSatisfying(f ->
                assertThat(f.getName()).isEqualTo("Sample1"));

        SystemTestDefCreator fooSystemCreator4 = new SystemTestDefCreator(fooSystem1, null, Sample1.class);
        assertThat(fooSystemCreator4).isNotNull();
        SystemTestDef fooSystem4 = fooSystemCreator1.create();
        assertThat(fooSystem4).isNotNull();
        assertThat(fooSystem4.getName()).isEqualTo("fooSystem");
        assertThat(fooSystem4.getFunctionTestDefs()).hasOnlyOneElementSatisfying(f ->
                assertThat(f.getName()).isEqualTo("Sample1"));
    }

    @Test
    public void createInvalidBaseResource() {
        SystemTestDefCreator fooSystemCreator = new SystemTestDefCreator("bazSystem");
        assertThatThrownBy(() -> fooSystemCreator.baseResource(SystemTestDefCreator.class, "Invalid.xml"))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(SAXParseException.class);

        fooSystemCreator.baseResource(SystemTestDefCreator.class, "Find-Test.xml");
        assertThat(fooSystemCreator.getBaseDef().getName()).isEqualTo("fooSystem");
        assertThat(fooSystemCreator.getBaseFile().toFile())
                .exists();
    }

    @Test
    public void createNotFoundBaseResource() {
        SystemTestDefCreator fooSystemCreator = new SystemTestDefCreator("fooSystem");
        assertThatThrownBy(() -> fooSystemCreator.baseResource(SystemTestDefCreator.class, "NotFound.xml"))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(NoSuchFileException.class);

    }

    @Test
    public void createInvalidBase() throws URISyntaxException {
        URL baseFileURL = SystemTestDefCreator.class.getResource("Invalid.xml");
        SystemTestDefCreator fooSystemCreator = new SystemTestDefCreator("bazSystem");
        assertThatThrownBy(() -> fooSystemCreator.base(Paths.get(baseFileURL.toURI())))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(SAXParseException.class);

        URL baseFileURL2 = SystemTestDefCreator.class.getResource("Find-Test.xml");
        fooSystemCreator.base(Paths.get(baseFileURL2.toURI()));
        assertThat(fooSystemCreator.getBaseDef().getName()).isEqualTo("fooSystem");
        assertThat(fooSystemCreator.getBaseFile().toFile())
                .exists();
    }


    @Test
    public void createNotFoundBase() {
        SystemTestDefCreator fooSystemCreator = new SystemTestDefCreator("fooSystem");
        assertThatThrownBy(() -> fooSystemCreator.base(Paths.get("NotFound.xml")))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(NoSuchFileException.class);
    }
}