package jogo.models.Structures;

/**
 * Defines the categories of architectural structures that can be built on the map grid.
 * Used by factory systems and serialization handlers to identify concrete building implementations.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public enum StructuresType {

    /** Represents a wood-producing forestry installation. */
    FOREST,

    /** Represents an administrative hub that expands baseline action potential and grants victory points. */
    CITY,

    /** Represents an agricultural livestock establishment that harvests food supplies. */
    RANCH,

    /** Represents an extraction site tailored for quarrying stone materials. */
    MINE
}