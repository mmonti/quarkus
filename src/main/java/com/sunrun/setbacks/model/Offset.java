package com.sunrun.setbacks.model;

import com.sunrun.util.Feet;

import org.slf4j.Logger;

public class Offset {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Offset.class);
    @Feet
    private final double offsetDistance;

    private final String rule;

    public Offset(double offsetDistance, String rule) {
        this.offsetDistance = offsetDistance;
        this.rule = rule;
    }

    public static OffsetBuilder builder() {
        return new OffsetBuilder();
    }

    public void validate(){
        if (offsetDistance < 0) {
            throw new IllegalArgumentException("offsetDistance must be greater or equal than 0");
        }
    }

    public double getOffsetDistance() {
        return this.offsetDistance;
    }

    public String getRule() {
        return this.rule;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Offset)) return false;
        final Offset other = (Offset) o;
        if (!other.canEqual((Object) this)) return false;
        if (Double.compare(this.getOffsetDistance(), other.getOffsetDistance()) != 0) return false;
        final Object this$rule = this.getRule();
        final Object other$rule = other.getRule();
        if (this$rule == null ? other$rule != null : !this$rule.equals(other$rule)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Offset;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $offsetDistance = Double.doubleToLongBits(this.getOffsetDistance());
        result = result * PRIME + (int) ($offsetDistance >>> 32 ^ $offsetDistance);
        final Object $rule = this.getRule();
        result = result * PRIME + ($rule == null ? 43 : $rule.hashCode());
        return result;
    }

    public String toString() {
        return "Offset(offsetDistance=" + this.getOffsetDistance() + ", rule=" + this.getRule() + ")";
    }

    public static class OffsetBuilder {
        private double offsetDistance;
        private String rule;

        OffsetBuilder() {
        }

        public Offset build() {
            final Offset offset = this.buildInternal();
            offset.validate();
            return offset;
        }

        public OffsetBuilder offsetDistance(double offsetDistance) {
            this.offsetDistance = offsetDistance;
            return this;
        }

        public OffsetBuilder rule(String rule) {
            this.rule = rule;
            return this;
        }

        public Offset buildInternal() {
            return new Offset(offsetDistance, rule);
        }

        public String toString() {
            return "Offset.OffsetBuilder(offsetDistance=" + this.offsetDistance + ", rule=" + this.rule + ")";
        }
    }
}
