package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.CartMapper;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public int addCart(String cartToken, ShopCart shopCart, User user) {

        //处理购物商品的小计
        Goods goods = goodsMapper.selectById(shopCart.getGid());
        BigDecimal numberDecimal = BigDecimal.valueOf(shopCart.getGnumber());
        //计算当前商品的小计
        shopCart.setAllprice(goods.getGprice().multiply(numberDecimal));

        if(user != null){
            //已经登录
            shopCart.setUid(user.getId());
            cartMapper.insert(shopCart);
        } else {
            //未登录 - redis
            //近 -> 远
            redisTemplate.opsForList().leftPush(cartToken, shopCart);
        }

        return 1;
    }

    /**
     * 查询购物车数据
     * @param cartToken
     * @param user
     * @return
     */
    @Override
    public List<ShopCart> queryCartsByUid(String cartToken, User user) {

        List<ShopCart> shopCarts = null;

        if(user != null){
            //去数据库查询购物车信息
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid", user.getId());

            shopCarts = cartMapper.selectList(queryWrapper);

        } else if(cartToken != null){
            //去redis查询购物车信息
            //查询链表的长度
            Long size = redisTemplate.opsForList().size(cartToken);
            //获取所有链表的数据
            shopCarts = redisTemplate.opsForList().range(cartToken, 0, size);
        }

        //根据购物车的商品id查询商品的详细信息
        if(shopCarts != null){
            for (ShopCart shopCart : shopCarts) {
                //购物车对应的商品信息
                Goods goods = goodsMapper.selectById(shopCart.getGid());
                //设置到购物车的对象中
                shopCart.setGoods(goods);
            }
        }

        return shopCarts;
    }

    /**
     * 合并购物车
     * @param cartToken
     * @param user
     * @return
     */
    @Override
    public int mergeCarts(String cartToken, User user) {

        if(cartToken != null){
            //获得临时购物车的信息
            Long size = redisTemplate.opsForList().size(cartToken);
            List<ShopCart> carts = redisTemplate.opsForList().range(cartToken, 0, size);

            if(carts == null){
                return 1;
            }

            //将临时购物车的数据保存到数据库中
            for (ShopCart cart : carts) {
                cart.setUid(user.getId());
                cartMapper.insert(cart);
            }

            //清空临时购物车
            redisTemplate.delete(cartToken);
        }

        return 1;
    }

    @Override
    public int deleteCartsByUid(int uid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", uid);
        return cartMapper.delete(queryWrapper);
    }
}
