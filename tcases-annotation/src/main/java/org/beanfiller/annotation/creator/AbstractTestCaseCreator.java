/* Copyright 2018 The Beanfiller Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.beanfiller.annotation.creator;

import org.beanfiller.annotation.generator.GeneratorSetBuilder;
import org.beanfiller.annotation.reader.AnnotatedFunctionDefReader;
import org.cornutum.tcases.SystemTestDef;
import org.cornutum.tcases.generator.GeneratorOptions;
import org.cornutum.tcases.generator.GeneratorSet;
import org.cornutum.tcases.generator.IGeneratorSet;
import org.cornutum.tcases.io.SystemTestDocReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Abstract builder for setting up the generation of Testcases from annotated classes.
 * Technical class to reduce duplicate code.
 */
abstract class AbstractTestCaseCreator<B extends AbstractTestCaseCreator<?>> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTestCaseCreator.class);

    private AnnotatedFunctionDefReader myReader;
    private IGeneratorSet myGeneratorSet;
    private GeneratorOptions myOptions;
    private SystemTestDef baseTestDef;
    private Path baseTestDefFile;
    private InstanceCreator myInstanceCreator;

    AbstractTestCaseCreator() {
    }

    AbstractTestCaseCreator(SystemTestDef baseDef) {
        this.baseTestDef = baseDef;
    }

    @Nullable
    static SystemTestDef getSystemTestDef(@Nullable String baseDefFilePath) {
        SystemTestDef baseDef = null;
        if (baseDefFilePath != null) {
            try (InputStream stream = Files.newInputStream(Paths.get(baseDefFilePath))) {
                baseDef = new SystemTestDocReader(stream).getSystemTestDef();
            } catch (RuntimeException rte) {
                if (rte.getCause() instanceof SAXParseException) {
                    logger.info("cannot parse system test def", rte);
                } else {
                    throw rte;
                }
            } catch (java.io.IOException ioe) {
                // pass
                logger.debug("File " + baseDefFilePath, ioe);
                throw new RuntimeException(ioe);
            }
        }
        return baseDef;
    }

    @Nullable
    static SystemTestDef getSystemTestDef(@Nullable URL baseFileURL) {
        SystemTestDef baseDef = null;
        if (baseFileURL != null) {
            try (InputStream stream = baseFileURL.openStream()) {
                baseDef = new SystemTestDocReader(stream).getSystemTestDef();
            } catch (RuntimeException rte) {
                if (rte.getCause() instanceof SAXParseException) {
                    logger.info("cannot parse system test def", rte);
                } else {
                    throw rte;
                }
            } catch (java.io.IOException ioe) {
                // pass
                logger.debug("URL " + baseFileURL, ioe);
                throw new RuntimeException(ioe);
            }
        }
        return baseDef;
    }

    @Nonnull
    protected abstract B getInstance();

    /**
     * looks up previous definitions from given file
     */
    public B baseResource(Class<?> clazz, String filename) {
        URL baseFileURL = clazz.getResource(filename);
        // if file exists
        if (baseFileURL != null) {
            baseTestDef = getSystemTestDef(baseFileURL);
            try {
                baseTestDefFile = Paths.get(baseFileURL.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return getInstance();
    }

    /**
     * looks up previous definitions from given file
     */
    public B base(@Nullable Path baseFileName) {
        if (baseFileName != null) {
            baseTestDef = getSystemTestDef(baseFileName.toString());
            baseTestDefFile = baseFileName;
        }
        return getInstance();
    }

    /**
     * uses given previous definitions
     */
    public B base(SystemTestDef base) {
        baseTestDef = base;
        return getInstance();
    }

    /**
     * configures Generators
     */
    @Nonnull
    public B generatorSet(IGeneratorSet generatorSet) {
        this.myGeneratorSet = generatorSet;
        return getInstance();
    }

    /**
     * allows custom Instance generation for fields
     */
    @Nonnull
    public B instanceCreator(InstanceCreator creator) {
        this.myInstanceCreator = creator;
        return getInstance();
    }

    @Nonnull
    public B tupleGenerator(int tuples) {
        this.myGeneratorSet = GeneratorSetBuilder.basicGeneratorSet(tuples).build();
        return getInstance();
    }

    /**
     * configures Generator Options
     */
    @Nonnull
    public B generatorOptions(GeneratorOptions options) {
        this.myOptions = options;
        return getInstance();
    }

    /**
     * Allows using custom Annotations
     */
    @Nonnull
    public B reader(AnnotatedFunctionDefReader reader) {
        this.myReader = reader;
        return getInstance();
    }

    @Nonnull
    protected IGeneratorSet getGeneratorSet() {
        if (myGeneratorSet == null) {
            myGeneratorSet = GeneratorSet.basicGenerator();
        }
        return myGeneratorSet;
    }

    @Nullable
    protected SystemTestDef getBaseDef() {
        return baseTestDef;
    }

    @Nullable
    protected Path getBaseFile() {
        return baseTestDefFile;
    }

    @Nonnull
    protected GeneratorOptions getOptions() {
        if (myOptions == null) {
            myOptions = new GeneratorOptions();
        }
        return myOptions;
    }

    @Nonnull
    protected InstanceCreator getInstanceCreator() {
        if (myInstanceCreator == null) {
            myInstanceCreator = ReflectionBasedInstanceCreator.withDefaultMapper();
        }
        return myInstanceCreator;
    }

    @Nonnull
    protected AnnotatedFunctionDefReader getReader() {
        if (myReader == null) {
            myReader = AnnotatedFunctionDefReader.withDefaultAnnotations();
        }
        return myReader;
    }


}
