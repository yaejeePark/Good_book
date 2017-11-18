package org.androidtown.goodbook;

public class ErrorDetector {
	public static ErrorCode checkGraphObject(LineGraphVO lineGraphVO){
		//1. vo check
		if(lineGraphVO == null){
			return ErrorCode.GRAPH_VO_IS_EMPTY;
		}
		
		//2. legend and graph size check
		int legendSize = lineGraphVO.getLegendArr().length;
		for (int i = 0; i < lineGraphVO.getArrGraph().size(); i++) {
			if(legendSize !=lineGraphVO.getArrGraph().get(i).getCoordinateArr().length){
				return ErrorCode.INVALIDATE_GRAPH_AND_LEGEND_SIZE;
			}
		}
		
		return ErrorCode.NOT_ERROR;
	}
	
	public static ErrorCode checkLineCompareGraphObject(LineGraphVO lineGraphVO){
		//1. vo check
		if(lineGraphVO == null){
			return ErrorCode.GRAPH_VO_IS_EMPTY;
		}

		
		//2 graph size must be 2
		if(lineGraphVO.getArrGraph().size() != 2){
			return ErrorCode.LINE_COMPARE_GRAPH_SIZE_MUST_BE_2;
		}
				
		//3. legend and graph size check
		int legendSize = lineGraphVO.getLegendArr().length;
		for (int i = 0; i < lineGraphVO.getArrGraph().size(); i++) {
			if(legendSize !=lineGraphVO.getArrGraph().get(i).getCoordinateArr().length){
				return ErrorCode.INVALIDATE_GRAPH_AND_LEGEND_SIZE;
			}
		}
		
		return ErrorCode.NOT_ERROR;
	}
	
//	public static ErrorCode checkGraphObject(RadarGraphVO radarGraphVO){
//		//1. vo check
//		if(radarGraphVO == null){
//			return ErrorCode.GRAPH_VO_IS_EMPTY;
//		}
//
//		//2. legend and graph size check
//		int legendSize = radarGraphVO.getLegendArr().length;
//		for (int i = 0; i < radarGraphVO.getArrGraph().size(); i++) {
//			if(legendSize !=radarGraphVO.getArrGraph().get(i).getCoordinateArr().length){
//				return ErrorCode.INVALIDATE_GRAPH_AND_LEGEND_SIZE;
//			}
//		}
//
//		return ErrorCode.NOT_ERROR;
//	}
//
	public static ErrorCode checkGraphObject(CurveGraphVO curveGraphVO){
		//1. vo check
		if(curveGraphVO == null){
			return ErrorCode.GRAPH_VO_IS_EMPTY;
		}
		
		//2. legend and graph size check
		int legendSize = curveGraphVO.getLegendArr().length;
		for (int i = 0; i < curveGraphVO.getArrGraph().size(); i++) {
			if(legendSize !=curveGraphVO.getArrGraph().get(i).getCoordinateArr().length){
				return ErrorCode.INVALIDATE_GRAPH_AND_LEGEND_SIZE;
			}
		}
		
		return ErrorCode.NOT_ERROR;
	}
	
	public static ErrorCode checkLineCompareGraphObject(CurveGraphVO curveGraphVO){
		//1. vo check
		if(curveGraphVO == null){
			return ErrorCode.GRAPH_VO_IS_EMPTY;
		}

		
		//2 graph size must be 2
		if(curveGraphVO.getArrGraph().size() != 2){
			return ErrorCode.LINE_COMPARE_GRAPH_SIZE_MUST_BE_2;
		}
				
		//3. legend and graph size check
		int legendSize = curveGraphVO.getLegendArr().length;
		for (int i = 0; i < curveGraphVO.getArrGraph().size(); i++) {
			if(legendSize !=curveGraphVO.getArrGraph().get(i).getCoordinateArr().length){
				return ErrorCode.INVALIDATE_GRAPH_AND_LEGEND_SIZE;
			}
		}
		
		return ErrorCode.NOT_ERROR;
	}
	
	public static ErrorCode checkGraphObject(CircleGraphVO circleGraphVO){
		//1. vo check
		if(circleGraphVO == null){
			return ErrorCode.GRAPH_VO_IS_EMPTY;
		}
		
		//2. legend and graph size check
		int arrSize = circleGraphVO.getArrGraph().size();
		if(arrSize == 0){
			return ErrorCode.GRAPH_VO_SIZE_ZERO;
		}
		
		return ErrorCode.NOT_ERROR;
	}
	
//	public static ErrorCode checkGraphObject(BubbleGraphVO bubbleGraphVO){
//		//1. vo check
//		if(bubbleGraphVO == null){
//			return ErrorCode.GRAPH_VO_IS_EMPTY;
//		}
//
//		//3. legend and graph size check
//		int legendSize = bubbleGraphVO.getLegendArr().length;
//		for (int i = 0; i < bubbleGraphVO.getArrGraph().size(); i++) {
//			if(legendSize !=bubbleGraphVO.getArrGraph().get(i).getCoordinateArr().length
//					|| legendSize !=bubbleGraphVO.getArrGraph().get(i).getSizeArr().length){
//				return ErrorCode.INVALIDATE_GRAPH_AND_LEGEND_SIZE;
//			}
//		}
//
//		return ErrorCode.NOT_ERROR;
//	}
	
	public static ErrorCode checkGraphObject(BarGraphVO barGraphVO){
		//1. vo check
		if(barGraphVO == null){
			return ErrorCode.GRAPH_VO_IS_EMPTY;
		}
		
		//2. legend and graph size check
		int legendSize = barGraphVO.getLegendArr().length;
		for (int i = 0; i < barGraphVO.getArrGraph().size(); i++) {
			if(legendSize !=barGraphVO.getArrGraph().get(i).getCoordinateArr().length){
				return ErrorCode.INVALIDATE_GRAPH_AND_LEGEND_SIZE;
			}
		}
		
		return ErrorCode.NOT_ERROR;
	}
}
