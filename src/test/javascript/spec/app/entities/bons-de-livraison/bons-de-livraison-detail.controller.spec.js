'use strict';

describe('Controller Tests', function() {

    describe('Bons_de_livraison Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBons_de_livraison, MockArticle, MockCommande;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBons_de_livraison = jasmine.createSpy('MockBons_de_livraison');
            MockArticle = jasmine.createSpy('MockArticle');
            MockCommande = jasmine.createSpy('MockCommande');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Bons_de_livraison': MockBons_de_livraison,
                'Article': MockArticle,
                'Commande': MockCommande
            };
            createController = function() {
                $injector.get('$controller')("Bons_de_livraisonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pfeApp:bons_de_livraisonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
