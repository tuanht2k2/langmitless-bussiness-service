package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.service.AssemblyAISpeechToText;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/audio")
public class AudioController {
  @PostMapping("/transcribe")
  public ResponseEntity<?> transcribeAudio(@RequestParam String fileUrl) {
    String transcriptId = AssemblyAISpeechToText.uploadAudio(fileUrl);

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    String text = AssemblyAISpeechToText.getTranscription(transcriptId);

    return ResponseEntity.ok(new CommonResponse<>(200, text, "Transcription success"));
  }
}
