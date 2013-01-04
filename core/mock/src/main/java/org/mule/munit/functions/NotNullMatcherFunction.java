package org.mule.munit.functions;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.munit.common.matchers.NotNullMatcher;

/**
 * <p>
 *     MEL function for {@link NotNullMatcher}
 *
 *     usage:
 *
 *     <pre>
 *         {@code
 *
 *           <script:script name="mockPayload" engine="groovy"><![CDATA[
 *                       return new String("anotherString");
 *                 ]]>
 *           </script:script>
 *
 *           <mock:verify-call messageProcessor="jira:create-group" atLeast="1">
 *                   <mock:attributes>
 *                           <mock:attribute name="userName" whereValue-ref='#[isNotNull()]'/>
 *                   </mock:attributes>
 *           </mock:verify-call>
 *         }
 *     </pre>
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class NotNullMatcherFunction implements ExpressionLanguageFunction {
    @Override
    public Object call(Object[] params, ExpressionLanguageContext context) {
        return new NotNullMatcher();
    }
}
