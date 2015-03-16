package fr.veridiangames.main.rendering.model;

import org.lwjgl.util.vector.Vector2f;

import fr.veridiangames.utils.Vector3f;

public class Vertex
{
    private Vector3f position, normal;
    private Vector2f texCoord;
    
    public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal)
    {
        this.position = position;
        this.texCoord = texCoord;
        this.normal = normal;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public Vector2f getTexCoord()
    {
        return texCoord;
    }
    
    public Vector3f getNormal()
    {
        return normal;
    }
}
