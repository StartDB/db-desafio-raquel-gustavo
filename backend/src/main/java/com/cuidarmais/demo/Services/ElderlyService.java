package com.cuidarmais.demo.Services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import com.cuidarmais.demo.DTO.ProfileDTO;
import com.cuidarmais.demo.DTO.UpdateUserDTO;
import com.cuidarmais.demo.DTO.UserResponseDTO;
import com.cuidarmais.demo.DTO.TaskDTO.TaskDTO;
import com.cuidarmais.demo.DTO.TaskDTO.TaskDTOTransform;
import com.cuidarmais.demo.Entities.Elderly;
import com.cuidarmais.demo.Infrastructure.Utils.TokenService;
import com.cuidarmais.demo.Repositories.ElderlyRepository;

import jakarta.transaction.Transactional;

@Service
public class ElderlyService {
    
    @Autowired
    public ElderlyRepository elderlyRepository;

    @Autowired
    TokenService tokenService;

    @Transactional
    public ResponseEntity<Object> saveElderly(Elderly elderly) {
        try {

        elderlyRepository.save(elderly);
        
        return ResponseEntity.ok("Cadastro salvo com sucesso!");

        } catch (DataIntegrityViolationException ex) {
        
        String detail = ex.getMostSpecificCause().getLocalizedMessage().split("Detail:")[1];
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detail);

        } catch (Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro desconhecido.");
    }
    }

    public List<Elderly> listAll() {
        return elderlyRepository.findAll();
    }

    public ResponseEntity<Object> getProfileById(Long id) {
        try {

            Optional<Elderly> elderlyOpt = elderlyRepository.findById(id);

            Elderly elderly = elderlyOpt.orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

            ProfileDTO profile = ProfileDTO.transformToProfileDTO(elderly);

            return ResponseEntity.ok(profile);

        } catch (NoSuchElementException ex) {
                
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

        } catch (Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro desconhecido.");
    }
        
    }

    public ResponseEntity<Object> updateElderly(UpdateUserDTO elderlyUpdate) {
        try {

            Optional<Elderly> elderlyOpt = elderlyRepository.findById(elderlyUpdate.id());

            Elderly elderly = elderlyOpt.orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

            Elderly finalElderly = UpdateUserDTO.mergeUpdateToElderly(elderlyUpdate, elderly);
            elderlyRepository.save(finalElderly);
            elderlyRepository.flush();

            UserResponseDTO userResponse = UserResponseDTO.transformToUserResponseDTO(finalElderly, tokenService.generateToken(finalElderly));
            
            return ResponseEntity.ok(userResponse);
    
            } catch (NoSuchElementException ex) {
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            
            } catch (DataIntegrityViolationException ex) {
            
            String detail = ex.getMostSpecificCause().getLocalizedMessage().split("Detail:")[1];
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detail);
    
            } catch (JpaSystemException ex) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro em atualizar os dados");

            } catch (Exception ex) {
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro desconhecido.");
        }
    }

    public ResponseEntity<Object> getMyTasks(Long id) {
       try {

            Optional<Elderly> elderlyOpt = elderlyRepository.findById(id);

            Elderly elderly = elderlyOpt.orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

            List<TaskDTO> taskList = TaskDTOTransform.transformToTaskDTOList(elderly.getTasks());

            return ResponseEntity.ok(taskList);

            } catch (NoSuchElementException ex) {
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

            } catch (Exception ex) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro desconhecido");
            }
    }
}
