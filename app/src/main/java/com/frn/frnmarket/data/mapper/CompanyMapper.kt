package com.frn.frnmarket.data.mapper

import com.frn.frnmarket.data.local.CompanyListingEntity
import com.frn.frnmarket.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing():CompanyListing{
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity():CompanyListingEntity{
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}