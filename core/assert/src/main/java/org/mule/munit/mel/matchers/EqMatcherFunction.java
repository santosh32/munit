package org.mule.munit.mel.matchers;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.munit.common.matchers.DumbMatcher;
import org.mule.munit.common.matchers.EqMatcher;


/**
 * <p>
 *     MEL function for the Munit {@link EqMatcher}
 *
 *     usage:
 *
 *     <pre>
 *         {@code
 *           <mock:verify-call messageProcessor="jira:create-group" atLeast="1">
 *                   <mock:attributes>
 *                           <mock:attribute name="userName" whereValue-ref='#[eq(#[string:name])]'/>
 *                   </mock:attributes>
 *           </mock:verify-call>
 *         }
 *     </pre>
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class EqMatcherFunction implements ExpressionLanguageFunction{
    @Override
    public Object call(final Object[] params, ExpressionLanguageContext context) {
        if ( params==null || params.length == 0 ){
            return new DumbMatcher(false);
        }
        return new EqMatcher(params[0]);
    }
}
