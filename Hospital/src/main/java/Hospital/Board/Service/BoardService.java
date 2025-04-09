package Hospital.Board.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import Hospital.Board.DTO.BoardDTO;
import Hospital.Board.DTO.BoardEditDTO;
import Hospital.Board.DTO.BoardEditResponseDTO;
import Hospital.Board.DTO.BoardListResponseDTO;
import Hospital.Board.DTO.BoardReadDTO;
import Hospital.Board.Entity.Board;
import Hospital.Board.Entity.BoardRepository;
import Hospital.Patient.Entity.Patient;
import jakarta.transaction.Transactional;
//글쓰기 Form에서 내용을 입력한 뒤, '글쓰기' 버튼을 누르면 Post 형식으로 요청이 오고, 
//BoardService의 savePost()를 실행한다.
//service 패키지를 만들고, 그 안에 BoardService 클래스 생성
@Service
public class BoardService {
	
	@Autowired
    private BoardRepository boardRepository;
	
    @Transactional
    public Integer SavePost(BoardDTO bDTO) {
        return boardRepository.save(bDTO.ToEntity()).getId();
    }

    
    @Transactional
    public List<BoardDTO> GetBoardList() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for(Board board : boardList) {
            BoardDTO boardDTO = BoardDTO.builder()
                    .Id(board.getId())
                    .Author(board.getAuthor())
                    .Title(board.getTitle())
                    .Content(board.getContent())
                    .CreatedDate(board.getCreatedDate())
                    .build();
            boardDTOList.add(boardDTO);
        }
        return boardDTOList;
    }
    
    
    public BoardReadDTO BoardRead(Integer Id) {
    	Board board = this.boardRepository.findById(Id).orElse(null);
    	return BoardReadDTO.BoardFactory(board);
    }
    
    
    
    public BoardEditResponseDTO BoardEdit(Integer Id) {
    	Board board = this.boardRepository.findById(Id).orElse(null);
    	return BoardEditResponseDTO.BoardFactory(board);
    }
    
    public void BoardUpdate(BoardEditDTO beDTO) {
    	Board board = this.boardRepository.findById(beDTO.getId()).orElse(null);
    	board = beDTO.Fill(board);
    	this.boardRepository.save(board);
    }
    
    
    public void BoardDelete(Integer Id) {
    	Board board = this.boardRepository.findById(Id).orElse(null);
    	if(board != null) {
    		this.boardRepository.deleteById(Id);
    	}
    }
    
    public List<BoardListResponseDTO> RecentBoardList() {
        List<Board> allboard = this.boardRepository.findAll();

        // 리스트가 비어 있는 경우 예외 방지
        if (allboard.isEmpty()) {
            System.out.println("게시글이 없습니다."); // 로그 출력
            return Collections.emptyList(); // 빈 리스트 반환
        }

        System.out.println(allboard.get(0)); // 예외 발생 없이 안전하게 실행

        List<BoardListResponseDTO> recentPosts = allboard.stream()
                .sorted(Comparator.comparing(Board::getCreatedDate).reversed()) // 최신순 정렬
                .limit(5)  // 최신 5개만 가져오기
                .map(board -> new BoardListResponseDTO(
                    board.getId(),
                    board.getAuthor(),
                    board.getTitle(),
                    board.getCreatedDate()
                ))  // DTO 변환
                .collect(Collectors.toList());

        return recentPosts;  // 반환값 추가
    }



    public List<BoardListResponseDTO> BoardListPage(Integer page, Integer maxsize) {
	    // 페이지 번호가 null이면 첫 페이지로 기본값 설정
	    if (page == null || page < 0) {
	        page = 0;
	    }
	    // 전체 데이터 조회
	    List<Board> allboard = this.boardRepository.findAll();  // 전체 데이터 조회
	    // 날짜를 기준으로 내림차순 정렬
	    allboard.sort((p1, p2) -> p2.getCreatedDate().compareTo(p1.getCreatedDate()));  // 내림차순 정렬
	    // 전체 데이터 개수
	    long totalElements = allboard.size();
	    // 전체 페이지 수 계산
	    int totalPages = (int) Math.ceil((double) totalElements / maxsize);
	    // 페이지네이션: 현재 페이지의 시작 인덱스 계산
	    int startIndex = page * maxsize;
	    int endIndex = Math.min(startIndex + maxsize, allboard.size());
	    // 현재 페이지에 해당하는 데이터만 잘라서 가져오기
	    List<Board> pagedPatients = allboard.subList(startIndex, endIndex);
	    // DTO로 변환하여 반환
	    return pagedPatients.stream()
	                         .map(board -> new BoardListResponseDTO(board.getId(), board.getAuthor(), board.getTitle(), board.getCreatedDate()))
	                         .collect(Collectors.toList());
	}
    
}
