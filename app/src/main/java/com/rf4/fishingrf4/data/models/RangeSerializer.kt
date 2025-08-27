package com.rf4.fishingrf4.data.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

// Ce "traducteur" explique comment sauvegarder et lire une plage de Double
object DoubleRangeSerializer : KSerializer<ClosedFloatingPointRange<Double>> {

    // On décrit comment la donnée sera structurée
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("DoubleRange") {
        element<Double>("start")
        element<Double>("endInclusive")
    }

    // Explique comment ÉCRIRE la donnée en JSON
    override fun serialize(encoder: Encoder, value: ClosedFloatingPointRange<Double>) {
        encoder.encodeStructure(descriptor) {
            encodeDoubleElement(descriptor, 0, value.start)
            encodeDoubleElement(descriptor, 1, value.endInclusive)
        }
    }

    // Explique comment LIRE la donnée depuis le JSON
    override fun deserialize(decoder: Decoder): ClosedFloatingPointRange<Double> {
        var start: Double? = null
        var end: Double? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> start = decodeDoubleElement(descriptor, 0)
                    1 -> end = decodeDoubleElement(descriptor, 1)
                    -1 -> break // Fin de la lecture
                }
            }
        }
        // On recrée la plage de type 0.5..15.0
        return start!!..end!!
    }
}