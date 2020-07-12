package np.com.susonthapa.ssotmovies.persistance

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by suson on 7/13/20
 */

@Entity
data class Movies(
    @PrimaryKey
    val id: String,
    val title: String,
    val year: String,
    val type: String,
    val image: String
)