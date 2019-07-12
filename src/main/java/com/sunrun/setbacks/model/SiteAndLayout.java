package com.sunrun.setbacks.model;

import org.slf4j.Logger;

import java.util.Map;

/**
 * This object is used for setback rules that require knowledge of the layouts and site model.
 */
public class SiteAndLayout {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SiteAndLayout.class);

    private final Site site;
    private final Map<RoofPlane, Layout> layouts;

    public SiteAndLayout(Site site, Map<RoofPlane, Layout> layouts) {
        this.site = site;
        this.layouts = layouts;
    }

    public static SiteAndLayoutBuilder builder() {
        return new SiteAndLayoutBuilder();
    }

    public Site getSite() {
        return this.site;
    }

    public Map<RoofPlane, Layout> getLayouts() {
        return this.layouts;
    }

    public String toString() {
        return "SiteAndLayout(site=" + this.getSite() + ", layouts=" + this.getLayouts() + ")";
    }

    public static class SiteAndLayoutBuilder {
        private Site site;
        private Map<RoofPlane, Layout> layouts;

        SiteAndLayoutBuilder() {
        }

        public SiteAndLayout.SiteAndLayoutBuilder site(Site site) {
            this.site = site;
            return this;
        }

        public SiteAndLayout.SiteAndLayoutBuilder layouts(Map<RoofPlane, Layout> layouts) {
            this.layouts = layouts;
            return this;
        }

        public SiteAndLayout build() {
            return new SiteAndLayout(site, layouts);
        }

        public String toString() {
            return "SiteAndLayout.SiteAndLayoutBuilder(site=" + this.site + ", layouts=" + this.layouts + ")";
        }
    }
}
