package codeladder.data;

import codeladder.data.dto.ExerciseFileDto;

record LoadedExerciseFile(String filePath, ExerciseFileDto fileDto) {
}
