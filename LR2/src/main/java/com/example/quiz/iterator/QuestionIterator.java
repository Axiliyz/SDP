package com.example.quiz.iterator;

import com.example.quiz.model.Question;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class QuestionIterator implements Iterator<Question> {
    private final List<Question> list;
    private int pos = 0;

    public QuestionIterator(List<Question> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return pos < list.size();
    }

    @Override
    public Question next() {
        if (!hasNext()) throw new NoSuchElementException();
        return list.get(pos++);
    }
}
