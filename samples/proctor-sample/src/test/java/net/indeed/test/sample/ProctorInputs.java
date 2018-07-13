package net.indeed.test.sample;

import org.beanfiller.annotation.annotations.Value;
import org.beanfiller.annotation.annotations.Var;

public class ProctorInputs {

    private static final String IS_MOBILE = "IS_MOBILE";

    // test input variables (request headers / url)

    @Var(value = {@Value("en"), @Value("fr"), @Value("ch")})
    public String language;

    @Var(value = {@Value("US"), @Value("FR"), @Value("JP")})
    public String country;

    @Var(value = {@Value(value = "true", properties = {IS_MOBILE})})
    public boolean mobile;

    @Var(value = {@Value(value = "true", when = IS_MOBILE)})
    public boolean native_mobile;

    // proctor test variables

    @Var(value = {
            @Value(value = "CONTROL", when = IS_MOBILE),
            @Value(value = "ACTIVE", when = IS_MOBILE)
    })
    public Buckets3 acme_cmp_search_noindex_tst;

    @Var
    public Buckets3 acme_discovery_privileged_tst;

    @Var
    public Buckets3 acme_footer_links_tst;

    // User behavior

    @Var
    public boolean userVisitsCompanySearch;

    @Var
    public boolean userVisitsCompanyReviews;


    public enum Buckets3 {
        INACTIVE,
        CONTROL,
        ACTIVE
        };

    @Override
    public String toString() {
        return "ProctorInputs{" +
                "language='" + language + '\'' +
                ", country='" + country + '\'' +
                ", mobile=" + mobile +
                ", native_mobile=" + native_mobile +
                ", acme_cmp_search_noindex_tst=" + acme_cmp_search_noindex_tst +
                ", acme_discovery_privileged_tst=" + acme_discovery_privileged_tst +
                ", acme_footer_links_tst=" + acme_footer_links_tst +
                ", userVisitsCompanySearch=" + userVisitsCompanySearch +
                ", userVisitsCompanyReviews=" + userVisitsCompanyReviews +
                '}';
    }
}
