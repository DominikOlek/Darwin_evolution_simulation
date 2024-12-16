package agh.ics.oop.Models.Utils;

import agh.ics.oop.Models.Utils.Vector2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2D> {
    private final int maxWidth;
    private final int maxHeight;
    private final int count;
    private final List<Vector2D> allPositions;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int count) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.count = count;
        this.allPositions = generateAllPositions();

        Collections.shuffle(allPositions);
    }

    private List<Vector2D> generateAllPositions() {
        List<Vector2D> positions = new ArrayList<>();
        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                positions.add(new Vector2D(x, y));
            }
        }
        return positions;
    }

    @Override
    public Iterator<Vector2D> iterator() {
        return new Iterator<Vector2D>() {
            private int generatedCount = 0;

            @Override
            public boolean hasNext() {
                return generatedCount < count;
            }

            @Override
            public Vector2D next() {
                if (!hasNext()) {
                    throw new UnsupportedOperationException("No more positions to generate");
                }

                Vector2D position = allPositions.get(generatedCount);
                generatedCount++;
                return position;
            }
        };
    }
}