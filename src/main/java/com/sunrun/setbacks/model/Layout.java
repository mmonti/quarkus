package com.sunrun.setbacks.model;

import org.slf4j.Logger;

//, "moduleSpecificationDTO"})
public class Layout {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Layout.class);
    private final int numModules;
    private final ModuleSpec moduleSpec;

    public Layout(int numModules, ModuleSpec moduleSpec) {
        this.numModules = numModules;
        this.moduleSpec = moduleSpec;
    }

    public int getNumModules() {
        return this.numModules;
    }

    public ModuleSpec getModuleSpec() {
        return this.moduleSpec;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Layout)) return false;
        final Layout other = (Layout) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getNumModules() != other.getNumModules()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Layout;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getNumModules();
        return result;
    }

    public String toString() {
        return "Layout(numModules=" + this.getNumModules() + ", moduleSpec=" + this.getModuleSpec() + ")";
    }
}
