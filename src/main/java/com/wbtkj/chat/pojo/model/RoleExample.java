package com.wbtkj.chat.pojo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoleExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RoleExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andAvatarIsNull() {
            addCriterion("avatar is null");
            return (Criteria) this;
        }

        public Criteria andAvatarIsNotNull() {
            addCriterion("avatar is not null");
            return (Criteria) this;
        }

        public Criteria andAvatarEqualTo(String value) {
            addCriterion("avatar =", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotEqualTo(String value) {
            addCriterion("avatar <>", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarGreaterThan(String value) {
            addCriterion("avatar >", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarGreaterThanOrEqualTo(String value) {
            addCriterion("avatar >=", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarLessThan(String value) {
            addCriterion("avatar <", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarLessThanOrEqualTo(String value) {
            addCriterion("avatar <=", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarLike(String value) {
            addCriterion("avatar like", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotLike(String value) {
            addCriterion("avatar not like", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarIn(List<String> values) {
            addCriterion("avatar in", values, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotIn(List<String> values) {
            addCriterion("avatar not in", values, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarBetween(String value1, String value2) {
            addCriterion("avatar between", value1, value2, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotBetween(String value1, String value2) {
            addCriterion("avatar not between", value1, value2, "avatar");
            return (Criteria) this;
        }

        public Criteria andNicknameIsNull() {
            addCriterion("nickname is null");
            return (Criteria) this;
        }

        public Criteria andNicknameIsNotNull() {
            addCriterion("nickname is not null");
            return (Criteria) this;
        }

        public Criteria andNicknameEqualTo(String value) {
            addCriterion("nickname =", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotEqualTo(String value) {
            addCriterion("nickname <>", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameGreaterThan(String value) {
            addCriterion("nickname >", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameGreaterThanOrEqualTo(String value) {
            addCriterion("nickname >=", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameLessThan(String value) {
            addCriterion("nickname <", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameLessThanOrEqualTo(String value) {
            addCriterion("nickname <=", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameLike(String value) {
            addCriterion("nickname like", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotLike(String value) {
            addCriterion("nickname not like", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameIn(List<String> values) {
            addCriterion("nickname in", values, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotIn(List<String> values) {
            addCriterion("nickname not in", values, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameBetween(String value1, String value2) {
            addCriterion("nickname between", value1, value2, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotBetween(String value1, String value2) {
            addCriterion("nickname not between", value1, value2, "nickname");
            return (Criteria) this;
        }

        public Criteria andGreetingIsNull() {
            addCriterion("greeting is null");
            return (Criteria) this;
        }

        public Criteria andGreetingIsNotNull() {
            addCriterion("greeting is not null");
            return (Criteria) this;
        }

        public Criteria andGreetingEqualTo(String value) {
            addCriterion("greeting =", value, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingNotEqualTo(String value) {
            addCriterion("greeting <>", value, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingGreaterThan(String value) {
            addCriterion("greeting >", value, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingGreaterThanOrEqualTo(String value) {
            addCriterion("greeting >=", value, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingLessThan(String value) {
            addCriterion("greeting <", value, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingLessThanOrEqualTo(String value) {
            addCriterion("greeting <=", value, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingLike(String value) {
            addCriterion("greeting like", value, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingNotLike(String value) {
            addCriterion("greeting not like", value, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingIn(List<String> values) {
            addCriterion("greeting in", values, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingNotIn(List<String> values) {
            addCriterion("greeting not in", values, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingBetween(String value1, String value2) {
            addCriterion("greeting between", value1, value2, "greeting");
            return (Criteria) this;
        }

        public Criteria andGreetingNotBetween(String value1, String value2) {
            addCriterion("greeting not between", value1, value2, "greeting");
            return (Criteria) this;
        }

        public Criteria andModelIsNull() {
            addCriterion("model is null");
            return (Criteria) this;
        }

        public Criteria andModelIsNotNull() {
            addCriterion("model is not null");
            return (Criteria) this;
        }

        public Criteria andModelEqualTo(String value) {
            addCriterion("model =", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotEqualTo(String value) {
            addCriterion("model <>", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThan(String value) {
            addCriterion("model >", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThanOrEqualTo(String value) {
            addCriterion("model >=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThan(String value) {
            addCriterion("model <", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThanOrEqualTo(String value) {
            addCriterion("model <=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLike(String value) {
            addCriterion("model like", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotLike(String value) {
            addCriterion("model not like", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelIn(List<String> values) {
            addCriterion("model in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotIn(List<String> values) {
            addCriterion("model not in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelBetween(String value1, String value2) {
            addCriterion("model between", value1, value2, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotBetween(String value1, String value2) {
            addCriterion("model not between", value1, value2, "model");
            return (Criteria) this;
        }

        public Criteria andSystemIsNull() {
            addCriterion("`system` is null");
            return (Criteria) this;
        }

        public Criteria andSystemIsNotNull() {
            addCriterion("`system` is not null");
            return (Criteria) this;
        }

        public Criteria andSystemEqualTo(String value) {
            addCriterion("`system` =", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemNotEqualTo(String value) {
            addCriterion("`system` <>", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemGreaterThan(String value) {
            addCriterion("`system` >", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemGreaterThanOrEqualTo(String value) {
            addCriterion("`system` >=", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemLessThan(String value) {
            addCriterion("`system` <", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemLessThanOrEqualTo(String value) {
            addCriterion("`system` <=", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemLike(String value) {
            addCriterion("`system` like", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemNotLike(String value) {
            addCriterion("`system` not like", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemIn(List<String> values) {
            addCriterion("`system` in", values, "system");
            return (Criteria) this;
        }

        public Criteria andSystemNotIn(List<String> values) {
            addCriterion("`system` not in", values, "system");
            return (Criteria) this;
        }

        public Criteria andSystemBetween(String value1, String value2) {
            addCriterion("`system` between", value1, value2, "system");
            return (Criteria) this;
        }

        public Criteria andSystemNotBetween(String value1, String value2) {
            addCriterion("`system` not between", value1, value2, "system");
            return (Criteria) this;
        }

        public Criteria andContextNIsNull() {
            addCriterion("context_n is null");
            return (Criteria) this;
        }

        public Criteria andContextNIsNotNull() {
            addCriterion("context_n is not null");
            return (Criteria) this;
        }

        public Criteria andContextNEqualTo(Integer value) {
            addCriterion("context_n =", value, "contextN");
            return (Criteria) this;
        }

        public Criteria andContextNNotEqualTo(Integer value) {
            addCriterion("context_n <>", value, "contextN");
            return (Criteria) this;
        }

        public Criteria andContextNGreaterThan(Integer value) {
            addCriterion("context_n >", value, "contextN");
            return (Criteria) this;
        }

        public Criteria andContextNGreaterThanOrEqualTo(Integer value) {
            addCriterion("context_n >=", value, "contextN");
            return (Criteria) this;
        }

        public Criteria andContextNLessThan(Integer value) {
            addCriterion("context_n <", value, "contextN");
            return (Criteria) this;
        }

        public Criteria andContextNLessThanOrEqualTo(Integer value) {
            addCriterion("context_n <=", value, "contextN");
            return (Criteria) this;
        }

        public Criteria andContextNIn(List<Integer> values) {
            addCriterion("context_n in", values, "contextN");
            return (Criteria) this;
        }

        public Criteria andContextNNotIn(List<Integer> values) {
            addCriterion("context_n not in", values, "contextN");
            return (Criteria) this;
        }

        public Criteria andContextNBetween(Integer value1, Integer value2) {
            addCriterion("context_n between", value1, value2, "contextN");
            return (Criteria) this;
        }

        public Criteria andContextNNotBetween(Integer value1, Integer value2) {
            addCriterion("context_n not between", value1, value2, "contextN");
            return (Criteria) this;
        }

        public Criteria andMaxTokensIsNull() {
            addCriterion("max_tokens is null");
            return (Criteria) this;
        }

        public Criteria andMaxTokensIsNotNull() {
            addCriterion("max_tokens is not null");
            return (Criteria) this;
        }

        public Criteria andMaxTokensEqualTo(Integer value) {
            addCriterion("max_tokens =", value, "maxTokens");
            return (Criteria) this;
        }

        public Criteria andMaxTokensNotEqualTo(Integer value) {
            addCriterion("max_tokens <>", value, "maxTokens");
            return (Criteria) this;
        }

        public Criteria andMaxTokensGreaterThan(Integer value) {
            addCriterion("max_tokens >", value, "maxTokens");
            return (Criteria) this;
        }

        public Criteria andMaxTokensGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_tokens >=", value, "maxTokens");
            return (Criteria) this;
        }

        public Criteria andMaxTokensLessThan(Integer value) {
            addCriterion("max_tokens <", value, "maxTokens");
            return (Criteria) this;
        }

        public Criteria andMaxTokensLessThanOrEqualTo(Integer value) {
            addCriterion("max_tokens <=", value, "maxTokens");
            return (Criteria) this;
        }

        public Criteria andMaxTokensIn(List<Integer> values) {
            addCriterion("max_tokens in", values, "maxTokens");
            return (Criteria) this;
        }

        public Criteria andMaxTokensNotIn(List<Integer> values) {
            addCriterion("max_tokens not in", values, "maxTokens");
            return (Criteria) this;
        }

        public Criteria andMaxTokensBetween(Integer value1, Integer value2) {
            addCriterion("max_tokens between", value1, value2, "maxTokens");
            return (Criteria) this;
        }

        public Criteria andMaxTokensNotBetween(Integer value1, Integer value2) {
            addCriterion("max_tokens not between", value1, value2, "maxTokens");
            return (Criteria) this;
        }

        public Criteria andTemperatureIsNull() {
            addCriterion("temperature is null");
            return (Criteria) this;
        }

        public Criteria andTemperatureIsNotNull() {
            addCriterion("temperature is not null");
            return (Criteria) this;
        }

        public Criteria andTemperatureEqualTo(Double value) {
            addCriterion("temperature =", value, "temperature");
            return (Criteria) this;
        }

        public Criteria andTemperatureNotEqualTo(Double value) {
            addCriterion("temperature <>", value, "temperature");
            return (Criteria) this;
        }

        public Criteria andTemperatureGreaterThan(Double value) {
            addCriterion("temperature >", value, "temperature");
            return (Criteria) this;
        }

        public Criteria andTemperatureGreaterThanOrEqualTo(Double value) {
            addCriterion("temperature >=", value, "temperature");
            return (Criteria) this;
        }

        public Criteria andTemperatureLessThan(Double value) {
            addCriterion("temperature <", value, "temperature");
            return (Criteria) this;
        }

        public Criteria andTemperatureLessThanOrEqualTo(Double value) {
            addCriterion("temperature <=", value, "temperature");
            return (Criteria) this;
        }

        public Criteria andTemperatureIn(List<Double> values) {
            addCriterion("temperature in", values, "temperature");
            return (Criteria) this;
        }

        public Criteria andTemperatureNotIn(List<Double> values) {
            addCriterion("temperature not in", values, "temperature");
            return (Criteria) this;
        }

        public Criteria andTemperatureBetween(Double value1, Double value2) {
            addCriterion("temperature between", value1, value2, "temperature");
            return (Criteria) this;
        }

        public Criteria andTemperatureNotBetween(Double value1, Double value2) {
            addCriterion("temperature not between", value1, value2, "temperature");
            return (Criteria) this;
        }

        public Criteria andTopPIsNull() {
            addCriterion("top_p is null");
            return (Criteria) this;
        }

        public Criteria andTopPIsNotNull() {
            addCriterion("top_p is not null");
            return (Criteria) this;
        }

        public Criteria andTopPEqualTo(Double value) {
            addCriterion("top_p =", value, "topP");
            return (Criteria) this;
        }

        public Criteria andTopPNotEqualTo(Double value) {
            addCriterion("top_p <>", value, "topP");
            return (Criteria) this;
        }

        public Criteria andTopPGreaterThan(Double value) {
            addCriterion("top_p >", value, "topP");
            return (Criteria) this;
        }

        public Criteria andTopPGreaterThanOrEqualTo(Double value) {
            addCriterion("top_p >=", value, "topP");
            return (Criteria) this;
        }

        public Criteria andTopPLessThan(Double value) {
            addCriterion("top_p <", value, "topP");
            return (Criteria) this;
        }

        public Criteria andTopPLessThanOrEqualTo(Double value) {
            addCriterion("top_p <=", value, "topP");
            return (Criteria) this;
        }

        public Criteria andTopPIn(List<Double> values) {
            addCriterion("top_p in", values, "topP");
            return (Criteria) this;
        }

        public Criteria andTopPNotIn(List<Double> values) {
            addCriterion("top_p not in", values, "topP");
            return (Criteria) this;
        }

        public Criteria andTopPBetween(Double value1, Double value2) {
            addCriterion("top_p between", value1, value2, "topP");
            return (Criteria) this;
        }

        public Criteria andTopPNotBetween(Double value1, Double value2) {
            addCriterion("top_p not between", value1, value2, "topP");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyIsNull() {
            addCriterion("frequency_penalty is null");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyIsNotNull() {
            addCriterion("frequency_penalty is not null");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyEqualTo(Double value) {
            addCriterion("frequency_penalty =", value, "frequencyPenalty");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyNotEqualTo(Double value) {
            addCriterion("frequency_penalty <>", value, "frequencyPenalty");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyGreaterThan(Double value) {
            addCriterion("frequency_penalty >", value, "frequencyPenalty");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyGreaterThanOrEqualTo(Double value) {
            addCriterion("frequency_penalty >=", value, "frequencyPenalty");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyLessThan(Double value) {
            addCriterion("frequency_penalty <", value, "frequencyPenalty");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyLessThanOrEqualTo(Double value) {
            addCriterion("frequency_penalty <=", value, "frequencyPenalty");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyIn(List<Double> values) {
            addCriterion("frequency_penalty in", values, "frequencyPenalty");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyNotIn(List<Double> values) {
            addCriterion("frequency_penalty not in", values, "frequencyPenalty");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyBetween(Double value1, Double value2) {
            addCriterion("frequency_penalty between", value1, value2, "frequencyPenalty");
            return (Criteria) this;
        }

        public Criteria andFrequencyPenaltyNotBetween(Double value1, Double value2) {
            addCriterion("frequency_penalty not between", value1, value2, "frequencyPenalty");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyIsNull() {
            addCriterion("presence_penalty is null");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyIsNotNull() {
            addCriterion("presence_penalty is not null");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyEqualTo(Double value) {
            addCriterion("presence_penalty =", value, "presencePenalty");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyNotEqualTo(Double value) {
            addCriterion("presence_penalty <>", value, "presencePenalty");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyGreaterThan(Double value) {
            addCriterion("presence_penalty >", value, "presencePenalty");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyGreaterThanOrEqualTo(Double value) {
            addCriterion("presence_penalty >=", value, "presencePenalty");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyLessThan(Double value) {
            addCriterion("presence_penalty <", value, "presencePenalty");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyLessThanOrEqualTo(Double value) {
            addCriterion("presence_penalty <=", value, "presencePenalty");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyIn(List<Double> values) {
            addCriterion("presence_penalty in", values, "presencePenalty");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyNotIn(List<Double> values) {
            addCriterion("presence_penalty not in", values, "presencePenalty");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyBetween(Double value1, Double value2) {
            addCriterion("presence_penalty between", value1, value2, "presencePenalty");
            return (Criteria) this;
        }

        public Criteria andPresencePenaltyNotBetween(Double value1, Double value2) {
            addCriterion("presence_penalty not between", value1, value2, "presencePenalty");
            return (Criteria) this;
        }

        public Criteria andLogitBiasIsNull() {
            addCriterion("logit_bias is null");
            return (Criteria) this;
        }

        public Criteria andLogitBiasIsNotNull() {
            addCriterion("logit_bias is not null");
            return (Criteria) this;
        }

        public Criteria andLogitBiasEqualTo(String value) {
            addCriterion("logit_bias =", value, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasNotEqualTo(String value) {
            addCriterion("logit_bias <>", value, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasGreaterThan(String value) {
            addCriterion("logit_bias >", value, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasGreaterThanOrEqualTo(String value) {
            addCriterion("logit_bias >=", value, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasLessThan(String value) {
            addCriterion("logit_bias <", value, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasLessThanOrEqualTo(String value) {
            addCriterion("logit_bias <=", value, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasLike(String value) {
            addCriterion("logit_bias like", value, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasNotLike(String value) {
            addCriterion("logit_bias not like", value, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasIn(List<String> values) {
            addCriterion("logit_bias in", values, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasNotIn(List<String> values) {
            addCriterion("logit_bias not in", values, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasBetween(String value1, String value2) {
            addCriterion("logit_bias between", value1, value2, "logitBias");
            return (Criteria) this;
        }

        public Criteria andLogitBiasNotBetween(String value1, String value2) {
            addCriterion("logit_bias not between", value1, value2, "logitBias");
            return (Criteria) this;
        }

        public Criteria andStopIsNull() {
            addCriterion("stop is null");
            return (Criteria) this;
        }

        public Criteria andStopIsNotNull() {
            addCriterion("stop is not null");
            return (Criteria) this;
        }

        public Criteria andStopEqualTo(String value) {
            addCriterion("stop =", value, "stop");
            return (Criteria) this;
        }

        public Criteria andStopNotEqualTo(String value) {
            addCriterion("stop <>", value, "stop");
            return (Criteria) this;
        }

        public Criteria andStopGreaterThan(String value) {
            addCriterion("stop >", value, "stop");
            return (Criteria) this;
        }

        public Criteria andStopGreaterThanOrEqualTo(String value) {
            addCriterion("stop >=", value, "stop");
            return (Criteria) this;
        }

        public Criteria andStopLessThan(String value) {
            addCriterion("stop <", value, "stop");
            return (Criteria) this;
        }

        public Criteria andStopLessThanOrEqualTo(String value) {
            addCriterion("stop <=", value, "stop");
            return (Criteria) this;
        }

        public Criteria andStopLike(String value) {
            addCriterion("stop like", value, "stop");
            return (Criteria) this;
        }

        public Criteria andStopNotLike(String value) {
            addCriterion("stop not like", value, "stop");
            return (Criteria) this;
        }

        public Criteria andStopIn(List<String> values) {
            addCriterion("stop in", values, "stop");
            return (Criteria) this;
        }

        public Criteria andStopNotIn(List<String> values) {
            addCriterion("stop not in", values, "stop");
            return (Criteria) this;
        }

        public Criteria andStopBetween(String value1, String value2) {
            addCriterion("stop between", value1, value2, "stop");
            return (Criteria) this;
        }

        public Criteria andStopNotBetween(String value1, String value2) {
            addCriterion("stop not between", value1, value2, "stop");
            return (Criteria) this;
        }

        public Criteria andIsMarketIsNull() {
            addCriterion("is_market is null");
            return (Criteria) this;
        }

        public Criteria andIsMarketIsNotNull() {
            addCriterion("is_market is not null");
            return (Criteria) this;
        }

        public Criteria andIsMarketEqualTo(Boolean value) {
            addCriterion("is_market =", value, "isMarket");
            return (Criteria) this;
        }

        public Criteria andIsMarketNotEqualTo(Boolean value) {
            addCriterion("is_market <>", value, "isMarket");
            return (Criteria) this;
        }

        public Criteria andIsMarketGreaterThan(Boolean value) {
            addCriterion("is_market >", value, "isMarket");
            return (Criteria) this;
        }

        public Criteria andIsMarketGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_market >=", value, "isMarket");
            return (Criteria) this;
        }

        public Criteria andIsMarketLessThan(Boolean value) {
            addCriterion("is_market <", value, "isMarket");
            return (Criteria) this;
        }

        public Criteria andIsMarketLessThanOrEqualTo(Boolean value) {
            addCriterion("is_market <=", value, "isMarket");
            return (Criteria) this;
        }

        public Criteria andIsMarketIn(List<Boolean> values) {
            addCriterion("is_market in", values, "isMarket");
            return (Criteria) this;
        }

        public Criteria andIsMarketNotIn(List<Boolean> values) {
            addCriterion("is_market not in", values, "isMarket");
            return (Criteria) this;
        }

        public Criteria andIsMarketBetween(Boolean value1, Boolean value2) {
            addCriterion("is_market between", value1, value2, "isMarket");
            return (Criteria) this;
        }

        public Criteria andIsMarketNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_market not between", value1, value2, "isMarket");
            return (Criteria) this;
        }

        public Criteria andMarketTypeIsNull() {
            addCriterion("market_type is null");
            return (Criteria) this;
        }

        public Criteria andMarketTypeIsNotNull() {
            addCriterion("market_type is not null");
            return (Criteria) this;
        }

        public Criteria andMarketTypeEqualTo(Integer value) {
            addCriterion("market_type =", value, "marketType");
            return (Criteria) this;
        }

        public Criteria andMarketTypeNotEqualTo(Integer value) {
            addCriterion("market_type <>", value, "marketType");
            return (Criteria) this;
        }

        public Criteria andMarketTypeGreaterThan(Integer value) {
            addCriterion("market_type >", value, "marketType");
            return (Criteria) this;
        }

        public Criteria andMarketTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("market_type >=", value, "marketType");
            return (Criteria) this;
        }

        public Criteria andMarketTypeLessThan(Integer value) {
            addCriterion("market_type <", value, "marketType");
            return (Criteria) this;
        }

        public Criteria andMarketTypeLessThanOrEqualTo(Integer value) {
            addCriterion("market_type <=", value, "marketType");
            return (Criteria) this;
        }

        public Criteria andMarketTypeIn(List<Integer> values) {
            addCriterion("market_type in", values, "marketType");
            return (Criteria) this;
        }

        public Criteria andMarketTypeNotIn(List<Integer> values) {
            addCriterion("market_type not in", values, "marketType");
            return (Criteria) this;
        }

        public Criteria andMarketTypeBetween(Integer value1, Integer value2) {
            addCriterion("market_type between", value1, value2, "marketType");
            return (Criteria) this;
        }

        public Criteria andMarketTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("market_type not between", value1, value2, "marketType");
            return (Criteria) this;
        }

        public Criteria andLikesIsNull() {
            addCriterion("likes is null");
            return (Criteria) this;
        }

        public Criteria andLikesIsNotNull() {
            addCriterion("likes is not null");
            return (Criteria) this;
        }

        public Criteria andLikesEqualTo(Integer value) {
            addCriterion("likes =", value, "likes");
            return (Criteria) this;
        }

        public Criteria andLikesNotEqualTo(Integer value) {
            addCriterion("likes <>", value, "likes");
            return (Criteria) this;
        }

        public Criteria andLikesGreaterThan(Integer value) {
            addCriterion("likes >", value, "likes");
            return (Criteria) this;
        }

        public Criteria andLikesGreaterThanOrEqualTo(Integer value) {
            addCriterion("likes >=", value, "likes");
            return (Criteria) this;
        }

        public Criteria andLikesLessThan(Integer value) {
            addCriterion("likes <", value, "likes");
            return (Criteria) this;
        }

        public Criteria andLikesLessThanOrEqualTo(Integer value) {
            addCriterion("likes <=", value, "likes");
            return (Criteria) this;
        }

        public Criteria andLikesIn(List<Integer> values) {
            addCriterion("likes in", values, "likes");
            return (Criteria) this;
        }

        public Criteria andLikesNotIn(List<Integer> values) {
            addCriterion("likes not in", values, "likes");
            return (Criteria) this;
        }

        public Criteria andLikesBetween(Integer value1, Integer value2) {
            addCriterion("likes between", value1, value2, "likes");
            return (Criteria) this;
        }

        public Criteria andLikesNotBetween(Integer value1, Integer value2) {
            addCriterion("likes not between", value1, value2, "likes");
            return (Criteria) this;
        }

        public Criteria andHotIsNull() {
            addCriterion("hot is null");
            return (Criteria) this;
        }

        public Criteria andHotIsNotNull() {
            addCriterion("hot is not null");
            return (Criteria) this;
        }

        public Criteria andHotEqualTo(Integer value) {
            addCriterion("hot =", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotNotEqualTo(Integer value) {
            addCriterion("hot <>", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotGreaterThan(Integer value) {
            addCriterion("hot >", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotGreaterThanOrEqualTo(Integer value) {
            addCriterion("hot >=", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotLessThan(Integer value) {
            addCriterion("hot <", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotLessThanOrEqualTo(Integer value) {
            addCriterion("hot <=", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotIn(List<Integer> values) {
            addCriterion("hot in", values, "hot");
            return (Criteria) this;
        }

        public Criteria andHotNotIn(List<Integer> values) {
            addCriterion("hot not in", values, "hot");
            return (Criteria) this;
        }

        public Criteria andHotBetween(Integer value1, Integer value2) {
            addCriterion("hot between", value1, value2, "hot");
            return (Criteria) this;
        }

        public Criteria andHotNotBetween(Integer value1, Integer value2) {
            addCriterion("hot not between", value1, value2, "hot");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}