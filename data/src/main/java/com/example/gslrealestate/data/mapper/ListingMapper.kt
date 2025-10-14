package com.example.gslrealestate.data.mapper

import com.example.gslrealestate.data.remote.dto.ListingDto
import com.example.gslrealestate.domain.model.Listing

/**
 * Mapper to convert between DTOs and Domain models
 * Following Single Responsibility Principle - handles only mapping logic
 */
object ListingMapper {
    /**
     * Converts a ListingDto to a domain Listing model
     * @param dto The DTO to convert
     * @return The domain model Listing
     */
    fun toDomain(dto: ListingDto): Listing {
        return Listing(
            id = dto.id,
            city = dto.city,
            area = dto.area,
            price = dto.price,
            professional = dto.professional,
            propertyType = dto.propertyType,
            offerType = dto.offerType,
            bedrooms = dto.bedrooms,
            rooms = dto.rooms,
            url = dto.url
        )
    }

    /**
     * Converts a list of ListingDto to a list of domain Listing models
     * @param dtoList The list of DTOs to convert
     * @return The list of domain model Listings
     */
    fun toDomainList(dtoList: List<ListingDto>): List<Listing> {
        return dtoList.map { toDomain(it) }
    }
}

