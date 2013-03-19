package org.mule.munit.common.mel.matchers;

import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageFunction;
import org.mule.munit.common.matchers.NullMatcher;


/**
 * <p>
 *     MEL function for {@link org.mule.munit.common.matchers.NullMatcher}
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
 *                           <mock:attribute name="userName" whereValue-ref='#[isNull()]'/>
 *                   </mock:attributes>
 *           </mock:verify-call>
 *         }
 *     </pre>
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class NullMatcherFunction implements ExpressionLanguageFunction {
    @Override
    public Object call(Object[] params, ExpressionLanguageContext context) {
        return new NullMatcher();
    }
}
