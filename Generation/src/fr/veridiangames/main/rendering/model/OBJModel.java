package fr.veridiangames.main.rendering.model;

import static org.lwjgl.opengl.GL11.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import fr.veridiangames.utils.Vector3f;

public class OBJModel
{
    List<Vector3f> positions;
    List<Vector2f> texCoord;
    List<Vector3f> normal;
    List<Triangle> triangles;
    List<Integer> indices;
    
    int list;

    public OBJModel(String path)
    {
        positions = new ArrayList<Vector3f>();
        texCoord = new ArrayList<Vector2f>();
        normal = new ArrayList<Vector3f>();
        triangles = new ArrayList<Triangle>();
        indices = new ArrayList<Integer>();
        
        loadModel(path);
        compile();
    }
    
    private void compile()
    {
        list = glGenLists(1);
        glNewList(list, GL_COMPILE);
        glBegin(GL_TRIANGLES);
        glColor4f(1, 1, 1, 1);
        for(int i = 0; i < triangles.size(); i++)
        {
            Vertex a = triangles.get(i).a;
            glNormal3f(a.getNormal().x, a.getNormal().y, a.getNormal().z);
            glTexCoord2f(a.getTexCoord().x, a.getTexCoord().y);
            glVertex3f(a.getPosition().x, a.getPosition().y, a.getPosition().z);
            
            Vertex b = triangles.get(i).b;
            glNormal3f(b.getNormal().x, b.getNormal().y, b.getNormal().z);
            glTexCoord2f(b.getTexCoord().x, b.getTexCoord().y);
            glVertex3f(b.getPosition().x, b.getPosition().y, b.getPosition().z);
            
            Vertex c = triangles.get(i).c;
            glNormal3f(c.getNormal().x, c.getNormal().y, c.getNormal().z);
            glTexCoord2f(c.getTexCoord().x, c.getTexCoord().y);
            glVertex3f(c.getPosition().x, c.getPosition().y, c.getPosition().z);
        }
        glEnd();
        glEndList();
    }
    
    private void loadModel(String path)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(OBJModel.class.getResourceAsStream(path)));
            String line = "";
            while((line = reader.readLine()) != null)
            {
                String[] values = line.split(" ");
                String prefix = values[0];
                
                if(prefix.equals("v"))
                {
                    float x = -Float.valueOf(values[1]);
                    float y = Float.valueOf(values[2]);
                    float z = Float.valueOf(values[3]);
                    positions.add(new Vector3f(x, y, z));
                }
                
                if(prefix.equals("vt"))
                {
                    float u = Float.valueOf(values[1]);
                    float v = (1-Float.valueOf(values[2]));
                    texCoord.add(new Vector2f(u, v));
                }
                
                if(prefix.equals("vn"))
                {
                    float x = Float.valueOf(values[1]);
                    float y = Float.valueOf(values[2]);
                    float z = Float.valueOf(values[3]);
                    normal.add(new Vector3f(x, y, z));
                }
                
                if(prefix.equals("f"))
                {
                    int p1 = Integer.parseInt(values[1].split("/")[0])-1;
                    int t1 = Integer.parseInt(values[1].split("/")[1])-1;
                    int n1 = Integer.parseInt(values[1].split("/")[2])-1;
                    Vertex a = new Vertex(positions.get(p1), texCoord.get(t1), normal.get(n1));
                    
                    int p2 = Integer.parseInt(values[2].split("/")[0])-1;
                    int t2 = Integer.parseInt(values[2].split("/")[1])-1;
                    int n2 = Integer.parseInt(values[2].split("/")[2])-1;
                    Vertex b = new Vertex(positions.get(p2), texCoord.get(t2), normal.get(n2));
                    
                    int p3 = Integer.parseInt(values[3].split("/")[0])-1;
                    int t3 = Integer.parseInt(values[3].split("/")[1])-1;
                    int n3 = Integer.parseInt(values[3].split("/")[2])-1;
                    Vertex c = new Vertex(positions.get(p3), texCoord.get(t3), normal.get(n3));
                    
                    triangles.add(new Triangle(a, b, c));

                    indices.add(p1);
                    indices.add(p2);
                    indices.add(p3);
                }
            }
            reader.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void render()
    {
        glCullFace(GL_FRONT);
        glCallList(list);
        glCullFace(GL_BACK);
    }

    public List<Vector3f> getPositions()
    {
        return positions;
    }

    public List<Vector2f> getTexCoord()
    {
        return texCoord;
    }

    public List<Vector3f> getNormal()
    {
        return normal;
    }

    public List<Triangle> getTriangles()
    {
        return triangles;
    }
    
    public List<Integer> getIndices()
    {
        return indices;
    }
}
