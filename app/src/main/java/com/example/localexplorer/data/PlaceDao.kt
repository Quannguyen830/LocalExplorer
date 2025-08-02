package com.example.localexplorer.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    
    @Query("SELECT * FROM places ORDER BY name ASC")
    fun getAllPlaces(): Flow<List<PlaceEntity>>
    
    @Query("SELECT * FROM places WHERE category = :category ORDER BY name ASC")
    fun getPlacesByCategory(category: String): Flow<List<PlaceEntity>>
    
    @Query("SELECT * FROM places WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoritePlaces(): Flow<List<PlaceEntity>>
    
    @Query("SELECT * FROM places WHERE id = :id")
    suspend fun getPlaceById(id: String): PlaceEntity?
    
    @Query("SELECT * FROM places WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchPlaces(searchQuery: String): Flow<List<PlaceEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: PlaceEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceEntity>)
    
    @Update
    suspend fun updatePlace(place: PlaceEntity)
    
    @Delete
    suspend fun deletePlace(place: PlaceEntity)
    
    @Query("UPDATE places SET isFavorite = :isFavorite WHERE id = :placeId")
    suspend fun updateFavoriteStatus(placeId: String, isFavorite: Boolean)
    
    @Query("DELETE FROM places")
    suspend fun deleteAllPlaces()
} 