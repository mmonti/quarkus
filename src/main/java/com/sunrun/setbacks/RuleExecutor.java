package com.sunrun.setbacks;

import com.sunrun.setbacks.model.*;

import java.util.List;
import java.util.Map;

/**
 * This interface encapsulates the Drools interaction.
 */
public interface RuleExecutor {

    List<Map<RoofEdge, Offset>> execute(final Site site, final GlobalOffsetMapping globalOffsetMapping,
                                        final RuleSet ruleSet);

}
