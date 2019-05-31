package com.berlejbej.saver.blocks;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Szymon on 2017-01-25.
 */
public class BlocksManager {

    private Context context;

    private static Block balanceBlock;
    private static Block remainingBlock;
    private static Block costsBlock;
    private static Block targetsBlock;

    public BlocksManager(Context context){
        this.context = context;
    }

    public void updateBlocks(){
        balanceBlock.update();
        remainingBlock.update();
        costsBlock.update();
        targetsBlock.update();
    }

    public void addBlocksToLayout(ViewGroup viewGroup){

        balanceBlock = new BalanceBlock(context);
        remainingBlock = new RemainingBlock(context);
        costsBlock = new CostsBlock(context);
        targetsBlock = new TargetsBlock(context);

        balanceBlock.create(viewGroup);
        remainingBlock.create(viewGroup);
        costsBlock.create(viewGroup);
        targetsBlock.create(viewGroup);
    }
}
