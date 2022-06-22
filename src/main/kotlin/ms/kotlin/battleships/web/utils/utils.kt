package ms.kotlin.battleships.web.utils

import javax.servlet.http.HttpServletRequest

val HttpServletRequest.serverPath: String
    get() = requestURL.toString().replace(servletPath, "")