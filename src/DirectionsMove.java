public enum DirectionsMove {
    left,
    up,
    right,
    down;

    DirectionsMove clockwise() {
        if (this.ordinal() < DirectionsMove.values().length - 1) {
            return DirectionsMove.values()[this.ordinal() + 1];
        } else return DirectionsMove.values()[0];
    }

    public DirectionsMove counterclockwise() {
        if (this.ordinal() > 0) {
            return DirectionsMove.values()[this.ordinal() - 1];
        } else return DirectionsMove.values()[DirectionsMove.values().length - 1];

    }

}
