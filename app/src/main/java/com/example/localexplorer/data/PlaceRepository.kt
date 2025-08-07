package com.example.localexplorer.data

import com.example.localexplorer.domain.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaceRepository(
    private val placeDao: PlaceDao
) {

    // get all places from database
    fun getAllPlaces(): Flow<List<Place>> {
        return placeDao.getAllPlaces().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    fun getPlacesByCategory(category: String): Flow<List<Place>> {
        return placeDao.getPlacesByCategory(category).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    fun getFavoritePlaces(): Flow<List<Place>> {
        return placeDao.getFavoritePlaces().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    suspend fun getPlaceById(id: String): Place? {
        return placeDao.getPlaceById(id)?.toDomainModel()
    }
    
    fun searchPlaces(query: String): Flow<List<Place>> {
        return placeDao.searchPlaces(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    suspend fun insertPlace(place: Place) {
        placeDao.insertPlace(place.toEntity())
    }
    
    suspend fun insertPlaces(places: List<Place>) {
        placeDao.insertPlaces(places.map { it.toEntity() })
    }
    
    suspend fun updateFavoriteStatus(placeId: String, isFavorite: Boolean) {
        placeDao.updateFavoriteStatus(placeId, isFavorite)
    }
    
    suspend fun deletePlace(place: Place) {
        placeDao.deletePlace(place.toEntity())
    }

    // this function doesn't do anything but keeping it just in case
    suspend fun seedInitialData() {
        // TODO: maybe implement this later
    }
}

// extension functions to convert between entity and domain models
private fun PlaceEntity.toDomainModel(): Place {
    return Place(
        id = id,
        name = name,
        description = description,
        latitude = latitude,
        longitude = longitude,
        imageUrl = imageUrl,
        category = category,
        isFavorite = isFavorite
    )
}

private fun Place.toEntity(): PlaceEntity {
    return PlaceEntity(
        id = id,
        name = name,
        description = description,
        latitude = latitude,
        longitude = longitude,
        imageUrl = imageUrl,
        category = category,
        isFavorite = isFavorite
    )
}