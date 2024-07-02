/*
 * [/legal-information/open-source-licenses](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/protocols/open_source_licenses.md)
 */
package com.ricoh360.thetaclient.transferred

import io.ktor.http.HttpMethod

/**
 * open-source-licenses api request
 */
internal object LicenseApi {
    const val PATH = "/legal-information/open-source-licenses"
    val METHOD = HttpMethod.Get
}
