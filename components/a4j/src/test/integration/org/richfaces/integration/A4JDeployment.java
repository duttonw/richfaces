package org.richfaces.integration;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.richfaces.arquillian.configuration.FundamentalTestConfiguration;
import org.richfaces.arquillian.configuration.FundamentalTestConfigurationContext;
import org.richfaces.deployment.BaseDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

public class A4JDeployment extends BaseDeployment {

    private final FundamentalTestConfiguration configuration = FundamentalTestConfigurationContext.getProxy();

    public A4JDeployment(Class<?> testClass) {
        this(testClass == null ? null : testClass.getSimpleName());
    }

    public A4JDeployment(String archiveName) {
        super(archiveName);

        if (configuration.isCurrentRichFacesVersion()) {

            addCurrentProjectClasses();

            this.addMavenDependency(
                    "com.github.albfernandez.richfaces:richfaces");

        } else {
            String version = configuration.getRichFacesVersion();
            this.addMavenDependency(
                    "com.github.albfernandez.richfaces:richfaces:" + version,
                    "com.github.albfernandez.richfaces:richfaces-a4j:" + version);
        }
    }

    private void addCurrentProjectClasses() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "richfaces-a4j.jar");
        jar.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
            .importDirectory("target/classes/").as(GenericArchive.class),
            "/", Filters.includeAll());
        archive().addAsLibrary(jar);
    }

    public FaceletAsset baseFacelet(String name) {
        FaceletAsset p = new FaceletAsset();

        this.archive().add(p, name);

        return p;
    }
}
