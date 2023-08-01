package com.example.noticeboard.service.event;

import com.example.noticeboard.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ModifyBoardEvent {
    private Board board;
}
