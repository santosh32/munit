package org.mule.munit.mel.matchers;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.modules.interceptor.matchers.AnyClassMatcher;
import org.mule.modules.interceptor.matchers.DumbMatcher;

/**
 * <p>
 *     MEL function for the Munit {@link AnyClassMatcher}
 *
 *     usage:
 *
 *     <pre>
 *         {@code
 *           <mock:verify-call messageProcessor="jira:create-group" atLeast="1">
 *                   <mock:attributes>
 *                           <mock:attribute name="userName" whereValue-ref='#[any()]'/>
 *                   </mock:attributes>
 *           </mock:verify-call>
 *         }
 *     </pre>
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class AnyClassMatcherFunction implements ExpressionLanguageFunction {
    @Override
    public Object call(final Object[] params, ExpressionLanguageContext context) {
        if ( params==null || params.length == 0 ){
            return new DumbMatcher(false);
        }
        if ( params[0] instanceof Class ){
            return new AnyClassMatcher((Class) params[0]);
        }
        return new DumbMatcher(false);

    }
}
