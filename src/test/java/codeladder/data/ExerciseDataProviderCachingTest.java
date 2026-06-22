package codeladder.data;

import codeladder.model.Exercise;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ExerciseDataProviderCachingTest {

    @Test
    void providerLoadsExercisesOnceAndReusesCachedList() {
        CountingExerciseJsonLoader loader = new CountingExerciseJsonLoader();
        ExerciseDataProvider provider = new ExerciseDataProvider(loader);

        List<Exercise> firstCall = provider.getExercises();
        List<Exercise> secondCall = provider.getExercises();

        assertEquals(1, loader.getLoadCount());
        assertSame(firstCall, secondCall);
    }

    private static final class CountingExerciseJsonLoader extends ExerciseJsonLoader {
        private int loadCount;
        private List<Exercise> cachedExercises;

        @Override
        public List<Exercise> loadExercises() {
            loadCount++;
            if (cachedExercises == null) {
                cachedExercises = super.loadExercises();
            }
            return cachedExercises;
        }

        int getLoadCount() {
            return loadCount;
        }
    }
}
