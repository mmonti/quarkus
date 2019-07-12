package com.sunrun.setbacks.model;

import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class RuleSet {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RuleSet.class);
    private Set<String> ruleIds;

    public RuleSet() {
        this(new HashSet<>());
    }

    public RuleSet(final Set<String> ruleIds) {
        if (ruleIds == null) {
            this.ruleIds = new HashSet<>();
        } else {
            this.ruleIds = ruleIds;
        }
    }

    public RuleSet add(final String ruleId) {
        if (ruleId == null) {
            throw new IllegalArgumentException("ruleId is null");
        }
        this.ruleIds.add(ruleId);
        return this;
    }

    public Set<String> getRuleIds() {
        return this.ruleIds;
    }

    public String toString() {
        return "RuleSet(ruleIds=" + this.getRuleIds() + ")";
    }
}
