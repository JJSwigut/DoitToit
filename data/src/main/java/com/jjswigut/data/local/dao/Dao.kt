package com.jjswigut.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity
import com.jjswigut.data.local.entities.relations.ListOfTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ListEntity)

    @Query("DELETE FROM list_table WHERE listId = :listId")
    suspend fun deleteList(listId: Long)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM list_table")
    fun getAllLists(): Flow<List<ListEntity>>

    @Transaction
    @Query("SELECT * FROM list_table WHERE listId = :listId")
    fun getListofTasks(listId: Long): Flow<List<ListOfTasks>>
}