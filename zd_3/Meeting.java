//autor: Jakub Marczyk 

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Meeting implements MeetingInterface {
	
	Position meetingPoint;
	public List<PawnPosition> pawnList;
	int pawnMoves;
	
	public void addPawns(List<PawnPosition> positions) {
		pawnList = new ArrayList<PawnPosition>();
		for(PawnPosition pos:positions) {
			pawnList.add(pos);
		}
	}

	public void addMeetingPoint(Position meetingPointPosition) {
		meetingPoint = new Position2D(meetingPointPosition.x(), meetingPointPosition.y());
	}

	public void move() {
		do {
			
			pawnMoves=0;
			for(PawnPosition pawn: pawnList) {
				//wyliczamy dx, dy - różnicę w położeniu X, Y pionka w stosunku do położenia punktu zbornego
				int dx = Math.abs(pawn.x()- meetingPoint.x());
				int dy = Math.abs(pawn.y()-meetingPoint.y());
				
				//jeśli |dx| > |dy| wykonujemy ruch w kierunku X o ile jest on możliwy (*)
				if(dx>dy) {
					if(pawn.x()>meetingPoint.x()) {
						pawnMoves+=updateSetAndList(pawn,-1,0);
					}
					else if(pawn.x()<meetingPoint.x()) {
						pawnMoves+=updateSetAndList(pawn,1,0);
					}
				}
				//jeśli |dx| <= |dy| wykonujemy ruch w kierunku Y o ile jest on możliwy (*)
				else if(dx<=dy) {
					if(pawn.y()>meetingPoint.y()) {
						pawnMoves+=updateSetAndList(pawn,0,-1);
					}
					else if(pawn.y()<meetingPoint.y()) {
						pawnMoves+=updateSetAndList(pawn,0,1);
					}
				
				}
			}
			Collections.reverse(pawnList);
		} while(pawnMoves>0);
	}
	
	int updateSetAndList(PawnPosition pawn, int x, int y){
		if(checkPosition(pawn.x()+x, pawn.y()+y)) {
			int index = pawnList.indexOf(pawn);
			PawnPosition temp = new PawnPosition2D(pawn.pawnId(), pawn.x()+x, pawn.y()+y);
			pawnList.set(index, temp);
			return 1;
		}
		return 0;
	}
	
	boolean checkPosition(int x, int y) {
		for(PawnPosition pos:pawnList) {
			if(pos.x()==x && pos.y()==y) {
				return false;
			}
		}
		return true;
	}

	public Set<PawnPosition> getAllPawns(){
		Set<PawnPosition> pawnSet = new HashSet<PawnPosition>();
		for(PawnPosition p: pawnList) {
			pawnSet.add(p);
		}
		return pawnSet;
	}

	public Set<PawnPosition> getNeighbours(int pawnId){
		PawnPosition pawn=null;
		for(PawnPosition neighbour:pawnList) {
			if(neighbour.pawnId()==pawnId) {
				pawn=neighbour;
			}
		}
		if(pawn!=null) {
			Set<PawnPosition> neighbourSet = new HashSet<PawnPosition>();
			for(PawnPosition neighbour:pawnList) {
				if(neighbour.pawnId()!=pawnId) {
					int dx=Math.abs(pawn.x()-neighbour.x());
					int dy=Math.abs(pawn.y()-neighbour.y());
					if(dx<=1 && dy<=1)
						neighbourSet.add(neighbour);
				}
			}
			return neighbourSet;
		}
		return null;
	}
}