<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
         <meta name="layout" content="main" />
        <title>Login</title>
    </head>
    <body>
        <div class="body">
            <h1>Login</h1>
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${user}">
                <div class="errors">
                    <g:renderErrors bean="${user}" as="list"/>
                </div>
            </g:hasErrors>
            <g:form action="doLogin" method="post">
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="email">Email:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: user, field: 'email', 'errors')}">
                                    <input type="text" id="email" name="email" value="${fieldValue(bean: user, field: 'email')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="password">Password:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: user, field: 'password', 'errors')}">
                                    <input type="password" id="password" name="password" />
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input type="image" src="${resource(dir:'images',file:'login.png')}" onsubmit="submit-form();"></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
