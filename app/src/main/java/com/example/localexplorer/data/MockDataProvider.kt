package com.example.localexplorer.data

import com.example.localexplorer.domain.Place

object MockDataProvider {
    
    fun getMockPlaces(): List<Place> {
        return listOf(
            Place(
                id = "1",
                name = "The Coffee Bean",
                description = "Cozy café with excellent espresso and homemade pastries. Perfect spot for working or catching up with friends.",
                latitude = -37.8136,
                longitude = 144.9631,
                imageUrl = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=800",
                category = "Café"
            ),
            Place(
                id = "2",
                name = "Brew & Beyond",
                description = "Artisan coffee roastery with specialty blends. Features local art and live music on weekends.",
                latitude = -37.8152,
                longitude = 144.9647,
                imageUrl = "https://images.unsplash.com/photo-1445116572660-236099ec97a0?w=800",
                category = "Café"
            ),
            Place(
                id = "3",
                name = "Royal Botanic Gardens",
                description = "Beautiful botanical gardens with diverse plant collections, peaceful walking paths, and stunning city views.",
                latitude = -37.8304,
                longitude = 144.9796,
                imageUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=800",
                category = "Park"
            ),
            Place(
                id = "4",
                name = "Fitzroy Gardens",
                description = "Historic gardens featuring beautiful tree-lined pathways, ornamental lakes, and the famous Fairies Tree.",
                latitude = -37.8129,
                longitude = 144.9803,
                imageUrl = "https://images.unsplash.com/photo-1574263867128-1ad8fdb7d51d?w=800",
                category = "Park"
            ),
            Place(
                id = "5",
                name = "Bella Vista Italian",
                description = "Authentic Italian cuisine with fresh pasta made daily. Romantic atmosphere with outdoor seating.",
                latitude = -37.8183,
                longitude = 144.9671,
                imageUrl = "https://images.unsplash.com/photo-1514933651103-005eec06c04b?w=800",
                category = "Restaurant"
            ),
            Place(
                id = "6",
                name = "Ocean's Bounty",
                description = "Fresh seafood restaurant with daily catches. Known for their fish and chips and seafood platters.",
                latitude = -37.8200,
                longitude = 144.9560,
                imageUrl = "https://images.unsplash.com/photo-1544025162-d76694265947?w=800",
                category = "Restaurant"
            ),
            Place(
                id = "7",
                name = "Melbourne Museum",
                description = "Interactive museum showcasing natural and cultural history. Features dinosaur exhibitions and indigenous culture.",
                latitude = -37.8033,
                longitude = 144.9717,
                imageUrl = "https://images.unsplash.com/photo-1565035010268-a3816f98589a?w=800",
                category = "Museum"
            ),
            Place(
                id = "8",
                name = "Immigration Museum",
                description = "Tells the stories of people from all over the world who have migrated to Victoria.",
                latitude = -37.8198,
                longitude = 144.9569,
                imageUrl = "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=800",
                category = "Museum"
            ),
            Place(
                id = "9",
                name = "Queen Victoria Market",
                description = "Historic market with fresh produce, gourmet foods, and unique souvenirs. A Melbourne institution since 1878.",
                latitude = -37.8076,
                longitude = 144.9568,
                imageUrl = "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=800",
                category = "Shopping"
            ),
            Place(
                id = "10",
                name = "Collins Street Shopping",
                description = "Premium shopping district with luxury boutiques, department stores, and designer outlets.",
                latitude = -37.8136,
                longitude = 144.9631,
                imageUrl = "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=800",
                category = "Shopping"
            ),
            Place(
                id = "11",
                name = "Crown Entertainment Complex",
                description = "Entertainment complex with casino, restaurants, bars, and live shows. Spectacular riverside location.",
                latitude = -37.8229,
                longitude = 144.9581,
                imageUrl = "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800",
                category = "Entertainment"
            ),
            Place(
                id = "12",
                name = "Luna Park Melbourne",
                description = "Historic amusement park with classic rides and carnival atmosphere. Fun for the whole family.",
                latitude = -37.8679,
                longitude = 144.9841,
                imageUrl = "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=800",
                category = "Entertainment"
            )
        )
    }
    
    fun getCategories(): List<String> {
        return listOf("All", "Café", "Restaurant", "Park", "Museum", "Shopping", "Entertainment")
    }
}