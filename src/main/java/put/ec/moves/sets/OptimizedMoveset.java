package put.ec.moves.sets;

import put.ec.moves.IntraMovesType;
import put.ec.moves.LocalMove;
import put.ec.solution.Solution;

import java.util.List;

public class OptimizedMoveset extends Moveset {
    public OptimizedMoveset(Solution solution, IntraMovesType movesType){
        super(solution,movesType,false);
    }

    @Override
    public List<LocalMove> getMoves() {
        

        return super.getMoves();
    }
}
