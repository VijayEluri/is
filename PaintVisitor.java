package ecv;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


class Bounds
{
	int left;
	int right;
}

class BoundsVisitor extends Visitor
{
	private int bottom;
	private ArrayList<Bounds> bounds = new ArrayList<Bounds>();

	private void visitOperator (Operator operator)
	{
		Bounds b = new Bounds ();
		bounds.add (b);
		int left = bounds.size();
		operator.getLeft().accept (this);
		int lbottom = bottom;
		b.left = bounds.get(left).left + bounds.get(left).right + PaintVisitor.H_SPACING;

		int rbottom = 0;
		if (operator.getRight() != null)
		{
			int right = bounds.size();
			operator.getRight().accept (this);
			rbottom = bottom;
			b.right = bounds.get(right).left + bounds.get(right).right + PaintVisitor.H_SPACING;
		}
		else
			b.right = PaintVisitor.BOX_WIDTH/2;
		bottom = 1 + (lbottom > rbottom ? lbottom : rbottom);
		// Be symmetric
		b.left = b.left > b.right ? b.left : b.right;
		b.right = b.left > b.right ? b.left : b.right;
	}

	public ArrayList<Bounds> getBounds () { return bounds; }
	public int getBottom () { return bottom; }

	public void visit (Number number)
	{
		Bounds b = new Bounds ();
		b.left = PaintVisitor.BOX_WIDTH/2;
		b.right = PaintVisitor.BOX_WIDTH/2;
		bounds.add (b);
		bottom = 1;
	}

	public void visit (Identifier identifier)
	{
		Bounds b = new Bounds ();
		b.left = PaintVisitor.BOX_WIDTH/2;
		b.right = PaintVisitor.BOX_WIDTH/2;
		bounds.add (b);
		bottom = 1;
	}

	public void visit (Operator.LogicalOperator logicalOperator)
	{
		visitOperator (logicalOperator);
	}

	public void visit (Operator.RelationalOperator relationalOperator)
	{
		visitOperator (relationalOperator);
	}

	public void visit (Operator.ArithmeticOperator arithmeticOperator)
	{
		visitOperator (arithmeticOperator);
	}
}

public class PaintVisitor extends Visitor
{
	static final int BOX_WIDTH = 50;
	static final int BOX_HEIGHT = 20;
	static final int H_SPACING = 4;
	static final int V_SPACING = 20;
	private Graphics2D g;
	private ArrayList<Bounds> bounds;
	private int bottom;
	private int index;

	public void visit (Number number)
	{
		index++;
		drawString (""+number.getValue ());
	}

	public void visit (Identifier identifier)
	{
		index++;
		drawString (identifier.getName ());
	}

	private void paintChildren (Operator operator)
	{
		AffineTransform transform = g.getTransform ();
		int cur = index;
		index++;
		g.setColor (Color.black);
		g.drawLine (-BOX_WIDTH/4, BOX_HEIGHT, -bounds.get(cur).left/2-H_SPACING, BOX_HEIGHT+V_SPACING);
		g.translate (-bounds.get(cur).left/2-H_SPACING, BOX_HEIGHT+V_SPACING);
		operator.getLeft().accept (this);
		g.setTransform (transform);

		if (operator.getRight() != null)
		{
			g.setColor (Color.black);
			g.drawLine (BOX_WIDTH/4, BOX_HEIGHT, bounds.get(cur).left/2+H_SPACING, BOX_HEIGHT+V_SPACING);
			g.translate (bounds.get(cur).right/2+H_SPACING, BOX_HEIGHT+V_SPACING);
			operator.getRight().accept (this);
			g.setTransform (transform);
		}
	}

	public void visit (Operator.LogicalOperator logicalOperator)
	{
		paintChildren (logicalOperator);

		String text = "";
		switch(logicalOperator.getType()) {
		case OP_AND:
			text = "&&";
			break;
		case OP_OR:
			text = "||";
			break;
		case OP_NOT:
			text = "!";
			break;
		default: assert false;
		}

		g.setColor (new Color (0x729fcf));
		g.fill3DRect (-BOX_WIDTH/3, 0, BOX_WIDTH/3*2, BOX_HEIGHT, true);    
		drawString (text);
	}

	public void visit (Operator.RelationalOperator relationalOperator)
	{
		paintChildren (relationalOperator);

		String text = null;
		switch(relationalOperator.getType()) {
		case OP_EQ:	text = "=="; break;
		case OP_NE:	text = "!="; break;
		case OP_GT:	text = ">"; break;
		case OP_GE:	text = ">="; break;
		case OP_LT:	text = "<"; break;
		case OP_LE:	text = "<="; break;
		default:		assert false;
		}
		g.setColor (new Color (0x8ae234));
		g.fillRoundRect (-BOX_WIDTH/3, 0, BOX_WIDTH/3*2, BOX_HEIGHT, 10, 10);
		drawString (text);
	}

	private void drawString (String text)
	{
		g.setColor (Color.black);
		Rectangle2D bounds = g.getFontMetrics().getStringBounds (text, g);
		g.drawString (text, (int)-bounds.getWidth()/2, (int)bounds.getHeight()/2 + BOX_HEIGHT/2 - g.getFontMetrics().getDescent());
	}

	public void visit (Operator.ArithmeticOperator arithmeticOperator)
	{
		paintChildren (arithmeticOperator);

		String text = null;
		switch(arithmeticOperator.getType()) {
		case OP_POW:	text = "^"; break;
		case OP_DIV:	text = "/"; break;
		case OP_MOD:	text = "%"; break;
		case OP_MUL:	text = "*"; break;
		case OP_ADD:	text = "+"; break;
		case OP_SUB:	text = "-"; break;
		default: 		assert false;
		}

		g.setColor (new Color (0xfcaf3e));
		g.fillRoundRect (-BOX_WIDTH/3, 0, BOX_WIDTH/3*2, BOX_HEIGHT, 10, 10);
		drawString (text);
	}

	public BufferedImage createImage (Expression expression)
	{
		boundsOf (expression);
		int width = bounds.get(0).left + bounds.get(0).right;
		int height = bottom*BOX_HEIGHT + (bottom-1)*V_SPACING;
		BufferedImage image = new BufferedImage (width, height+V_SPACING*2, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics ();
		g.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.translate (bounds.get(0).left, V_SPACING);
		expression.accept (this);
		return image;
	}

	private void boundsOf (Expression expression)
	{
		BoundsVisitor visitor = new BoundsVisitor ();
		expression.accept (visitor);
		bounds = visitor.getBounds ();
		bottom = visitor.getBottom ();
	}
}