package com.proj.bootiestrappi.controller

import com.proj.bootiestrappi.Database.model.Note
import com.proj.bootiestrappi.Database.repo.noteRepo
import jakarta.websocket.server.PathParam
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController (
    private val repository:noteRepo
){
    data class NoteResponse(
        val title:String,
        val content:String,
        val id:String,
        val color:Long,
        val createdat:Instant,
        val ownerId: String
    )
    data class NoteRequest(
        val title:String,
        val content:String,
        val id:String?,
        val color:Long,
    )
    @PostMapping
    fun saveNote(@RequestBody body:NoteRequest):NoteResponse{
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val note = repository.save(Note(
            title = body.title,
            color = body.color,
            content = body.content,
            id = body.id?.let { ObjectId(it) }?:ObjectId.get(),
            createdAt = Instant.now(),
            ownerId = ObjectId(ownerId)
        ))
        return note.toNoteResponse()
    }
    @GetMapping
    fun findByOwnerId():List<NoteResponse>{
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return repository.findByOwnerId(ObjectId(ownerId)).map {
            it?.toNoteResponse()!!
        }
    }
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id:String){
        val note = repository.findById(ObjectId(id)).orElseThrow{ IllegalArgumentException("Note Doesn't exist")}
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        if(note.ownerId.toHexString() == ownerId){
            repository.deleteById(ObjectId(id))
        }
    }
}

private fun Note.toNoteResponse():NoteController.NoteResponse{
    return NoteController.NoteResponse(
        title = title,
        content = content,
        color = color,
        id = id.toHexString(),
        createdat = Instant.now(),
        ownerId = ownerId.toHexString()
    )
}