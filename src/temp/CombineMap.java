package temp;

public class CombineMap implements TempMap {
	TempMap tmap1, tmap2;
	public String tempMap(temp.Temp t) {
	   /*String s = tmap1.TempMap(t);
	   if (s!=null) return s;
	   return tmap2.TempMap(t);*/
	return null;	
	}

	public CombineMap(TempMap t1, TempMap t2) {
	   tmap1=t1; tmap2=t2;
	}
}
