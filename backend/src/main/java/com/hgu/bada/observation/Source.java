package com.hgu.bada.observation;

// 관측 기록이 어디서 왔는지
public enum Source {
    MANUAL,    // 관리자가 직접 입력
    KHOA_TIDE, // 국립해양조사원 조위관측소 (수온·조위)
    KMA_BUOY   // 기상청 해양기상부이 (파고·풍속)
}
