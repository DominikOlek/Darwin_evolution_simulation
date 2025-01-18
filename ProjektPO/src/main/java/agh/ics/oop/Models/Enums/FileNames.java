package agh.ics.oop.Models.Enums;

public enum FileNames{
    Weak,
    Medium,
    Strong,
    Sgrass,
    Lgrass;

    public String toString() {
        return switch (this) {
            case Weak     -> "weak.png";
            case Medium -> "medium.png";
            case Strong      -> "strong.png";
            case Sgrass -> "Sgrass.png";
            case Lgrass     -> "Lgrass.png";
        };
    }
}

