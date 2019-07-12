package com.sunrun.setbacks.rs;

import com.sunrun.setbacks.model.EdgeType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetbackRequest {

    private List<LayoutPlaneInput> layoutPlaneInput;
    private Set<String> ruleIds;
    private Map<EdgeType, Double> globalOffsetMapping;

    public SetbackRequest(final List<LayoutPlaneInput> layoutPlaneInput, final Set<String> ruleIds,
                          final Map<EdgeType, Double> globalOffsetMapping) {

        this.layoutPlaneInput = layoutPlaneInput;
        this.ruleIds = ruleIds;
        this.globalOffsetMapping = globalOffsetMapping;
    }

    public SetbackRequest() {
    }

    public void validate(){
        if (layoutPlaneInput.isEmpty()) {
            throw new IllegalArgumentException("layoutPlaneInput cannot be empty");
        }
    }

    public List<LayoutPlaneInput> getLayoutPlaneInput() {
        return this.layoutPlaneInput;
    }

    public Set<String> getRuleIds() {
        return this.ruleIds;
    }

    public Map<EdgeType, Double> getGlobalOffsetMapping() {
        return this.globalOffsetMapping;
    }

    public void setLayoutPlaneInput(List<LayoutPlaneInput> layoutPlaneInput) {
        this.layoutPlaneInput = layoutPlaneInput;
    }

    public void setRuleIds(Set<String> ruleIds) {
        this.ruleIds = ruleIds;
    }

    public void setGlobalOffsetMapping(Map<EdgeType, Double> globalOffsetMapping) {
        this.globalOffsetMapping = globalOffsetMapping;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SetbackRequest)) return false;
        final SetbackRequest other = (SetbackRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$layoutPlaneInput = this.getLayoutPlaneInput();
        final Object other$layoutPlaneInput = other.getLayoutPlaneInput();
        if (this$layoutPlaneInput == null ? other$layoutPlaneInput != null : !this$layoutPlaneInput.equals(other$layoutPlaneInput))
            return false;
        final Object this$ruleIds = this.getRuleIds();
        final Object other$ruleIds = other.getRuleIds();
        if (this$ruleIds == null ? other$ruleIds != null : !this$ruleIds.equals(other$ruleIds)) return false;
        final Object this$globalOffsetMapping = this.getGlobalOffsetMapping();
        final Object other$globalOffsetMapping = other.getGlobalOffsetMapping();
        if (this$globalOffsetMapping == null ? other$globalOffsetMapping != null : !this$globalOffsetMapping.equals(other$globalOffsetMapping))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SetbackRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $layoutPlaneInput = this.getLayoutPlaneInput();
        result = result * PRIME + ($layoutPlaneInput == null ? 43 : $layoutPlaneInput.hashCode());
        final Object $ruleIds = this.getRuleIds();
        result = result * PRIME + ($ruleIds == null ? 43 : $ruleIds.hashCode());
        final Object $globalOffsetMapping = this.getGlobalOffsetMapping();
        result = result * PRIME + ($globalOffsetMapping == null ? 43 : $globalOffsetMapping.hashCode());
        return result;
    }

    public String toString() {
        return "SetbackRequest(layoutPlaneInput=" + this.getLayoutPlaneInput() + ", ruleIds=" + this.getRuleIds() + ", globalOffsetMapping=" + this.getGlobalOffsetMapping() + ")";
    }
}